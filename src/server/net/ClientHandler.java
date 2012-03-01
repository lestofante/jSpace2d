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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public ClientHandler(int MTU) {
		this.MTU = MTU;

		try {
			reader = Selector.open();
			writer = Selector.open();
		} catch (IOException e) {
			log.error("Failed opening a selector", e);
		}
	}

	private RemoveNetworkPlayer disconnectAndRemove(SelectionKey key) {
		try {
			key.channel().close();
		} catch (IOException e) {
			log.error("Error removing player", e);
		}

		key.cancel();

		log.info("Disconnected player: {}", key.attachment());

		return new RemoveNetworkPlayer(key);
	}

	private NetworkWorker readWorker(SelectionKey key) throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(MTU);
		buf.clear();
		SocketChannel channel = (SocketChannel) key.channel();
		int numBytesRead = channel.read(buf);

		if (numBytesRead == -1) {
			throw new Exception("Read EOF from channel");
		}

		buf.flip();
		TCP_Packet packet = null;

		try {
			packet = PacketRecognizer.getTCP(buf);
		} catch (Exception e) {
			log.error("Error recognizing packet", e);
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
			log.info("New client connected: {}", clientChannel.getRemoteAddress());
		} catch (IOException e) {
			log.error("Error adding client", e);
		}
	}

	public void read(ArrayList<Worker> w) {
		try {
			reader.selectNow();
		} catch (IOException e) {
			e.printStackTrace();
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
					log.error("Error reading from channel", e);
				} catch (Exception e) {
					w.add(disconnectAndRemove(key));
					log.error("Error reading from channel", e);
				}
			}
			keyIterator.remove();
		}
	}
}
