package server.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.net.worker.S_RemoveNetworkPlayer;
import base.game.network.packets.PacketHandler;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP_Packet.TCP_PacketType;
import base.game.player.NetworkPlayer;
import base.worker.Worker;

public class ClientHandler {
	/*
	 * innanzitutto si chiama nel modo sbagliato :P è ancora il vecchio selector
	 * handler, anche se ci sono già quasi tutti i metodi che ci servono
	 * immagino che quello che si occupa dei login sia praticamente identico in
	 * realtà è piuttosto diverso
	 */

	Selector reader = null;

	private final int MTU;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public ClientHandler(int MTU) {
		this.MTU = MTU;

		try {
			reader = Selector.open();
		} catch (IOException e) {
			log.error("Failed opening a selector", e);
		}
	}

	private S_RemoveNetworkPlayer disconnectAndRemove(NetworkPlayer player) {
		return new S_RemoveNetworkPlayer(player);
	}

	private Worker readWorker(SelectionKey key) throws Exception {
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
			packet = PacketHandler.getTCP(buf);
		} catch (Exception e) {
			log.error("Error recognizing packet", e);
		}

		if (packet != null) {
			if (packet.PacketType != TCP_PacketType.LOGIN) {
				// TODO implement packets
			} else {
				throw new Exception("Address already logged in");
			}
		} else {
			throw new Exception("Error handling packet");
		}

		return null;

	}

	public SelectionKey addConnectedClient(SocketChannel clientChannel) throws ClosedChannelException {
		SelectionKey key = clientChannel.register(reader, SelectionKey.OP_READ);
		try {
			log.info("New client connected: {}", clientChannel.getRemoteAddress());
		} catch (IOException e) {
			log.error("Error adding client", e);
		}
		return key;
	}

	public void read(ArrayList<Worker> w) {
		try {
			reader.selectNow();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Set<SelectionKey> selectedKeys = reader.selectedKeys();

		Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

		Worker input;
		SelectionKey key;

		while (keyIterator.hasNext()) {
			key = keyIterator.next();
			if (key.isReadable() && key.isValid()) {
				try {
					input = readWorker(key);
					if (input != null)
						w.add(input);
				} catch (IOException e) {
					log.error("Error reading from channel", e);
					key.cancel();
					w.add(disconnectAndRemove((NetworkPlayer) key.attachment()));
				} catch (Exception e) {
					log.error("Error reading from channel", e);
					w.add(disconnectAndRemove((NetworkPlayer) key.attachment()));
					key.cancel();
				}
			}
			keyIterator.remove();
		}
	}

	public void write(HashMap<NetworkPlayer, ArrayList<TCP_Packet>> wOUT, ArrayList<Worker> wIN) {
		for (NetworkPlayer player : wOUT.keySet()) {
			try {
				for (TCP_Packet p : wOUT.get(player)) {
					log.debug("writing tcp_packet to: {}", player.getPlayerName());
					((SocketChannel) player.getKey().channel()).write(p.getDataBuffer());
				}
				wOUT.get(player).clear();
			} catch (IOException e) {
				wIN.add(new S_RemoveNetworkPlayer(player));
				log.error("Error writing to player {}: disconnected", player.getPlayerName(), e);
			}
		}
	}
}
