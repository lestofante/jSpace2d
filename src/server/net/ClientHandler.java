package server.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import base.game.network.packets.PacketRecognizer;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP_Packet.PacketType;
import base.game.player.Player;
import base.game.player.worker.RemoveNetworkPlayer;
import base.worker.NetworkWorker;
import base.worker.Worker;

public class ClientHandler {
	/*
	 * innanzitutto si chiama nel modo sbagliato :P è ancora il vecchio selector
	 * handler, anche se ci sono già quasi tutti i metodi che ci servono
	 * immagino che quello che si occupa dei login sia praticamente identico in
	 * realtà è piuttosto diverso
	 */

	Selector reader = null;
	Selector writer = null;

	private final int MTU;

	public ClientHandler(int MTU) {
		this.MTU = MTU;

		try {
			reader = Selector.open();
			writer = Selector.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private RemoveNetworkPlayer disconnectAndRemove(SelectionKey key) {
		try {
			key.channel().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		key.cancel();

		System.out.println("Disconnected player: " + key.attachment());

		return new RemoveNetworkPlayer(key);
	}

	private NetworkWorker readWorker(SelectionKey key) throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(MTU);
		buf.clear();
		SocketChannel channel = (SocketChannel) key.channel();
		int numBytesRead = channel.read(buf);

		if (numBytesRead == -1) {
			throw new Exception("Error reading from channel");
		}

		buf.flip();
		TCP_Packet packet = null;

		try {
			packet = PacketRecognizer.getTCP(buf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (packet != null) {
			if (packet.packetType != PacketType.LOGIN) {
				// TODO implement packets
			} else {
				throw new Exception("Address already logged in");
			}
		} else {
			throw new Exception("Error handling packet");
		}

		return null;

	}

	public void addConnectedClient(SocketChannel clientChannel, Player player) throws ClosedChannelException {
		clientChannel.register(reader, SelectionKey.OP_READ, player);
		clientChannel.register(writer, SelectionKey.OP_WRITE, player);
		try {
			System.out.println("New channel connected: " + clientChannel.getRemoteAddress());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void read(ArrayList<Worker> w) {
		try {
			reader.selectNow();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Set<SelectionKey> selectedKeys = reader.selectedKeys();

		Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

		NetworkWorker input;
		SelectionKey key;

		while (keyIterator.hasNext()) {
			key = keyIterator.next();
			if (key.isReadable() && key.isValid()) {
				try {
					input = readWorker(key);
					if (input != null)
						w.add(input);
				} catch (IOException e) {
					w.add(disconnectAndRemove(key));
					e.printStackTrace();
				} catch (Exception e) {
					w.add(disconnectAndRemove(key));
					e.printStackTrace();
				}
			}
			keyIterator.remove();
		}
	}

}
