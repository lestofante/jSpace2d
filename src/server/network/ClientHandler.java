package server.network;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.network.worker.EntityRequest;
import server.network.worker.PingUpdate;
import server.network.worker.PlayerAction;
import server.network.worker.S_RemoveNetworkPlayer;
import server.worker.ServerWorker;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP.toServer.ClientActionPacket;
import base.game.network.packets.TCP.toServer.PingResponsePacket;
import base.game.network.packets.TCP.toServer.RequestEntityPacket;

public class ClientHandler {

	private static final int MAX_PACKET_PER_TURN = 50000;

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

	private S_RemoveNetworkPlayer disconnectAndRemove(ServerNetworkStream stream) {
		try {
			stream.getChannel().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new S_RemoveNetworkPlayer(stream);
	}

	private int readWorker(SelectionKey key, List<ServerWorker> ris) throws Exception {
		ServerNetworkStream stream = (ServerNetworkStream) key.attachment();
		if (!stream.update()) {
			// lost connection to client
			return -1;
		}

		if (stream.available.size() > MAX_PACKET_PER_TURN) {
			throw new Exception("Client is trying to flood. Detected: " + stream.available.size() + " packets");
		}

		// log.debug("Read {} packets in one bott", stream.available.size());

		while (!stream.available.isEmpty()) {
			TCP_Packet packet = stream.available.remove();

			switch (packet.PacketType) {
			case CLIENT_ACTION:
				ris.add(new PlayerAction((ClientActionPacket) packet));
				break;
			case REQUEST:
				ris.add(new EntityRequest((RequestEntityPacket) packet, stream.getConnectedPlayer()));
				break;
			case LOGIN:
				throw new Exception("Address already logged in");
			case PING_RESPONSE:
				ris.add(new PingUpdate((PingResponsePacket) packet));
				break;
			default:
				throw new Exception("Unkonw requested action!");
			}
		}

		return 0;

	}

	public SelectionKey addConnectedClient(ServerNetworkStream stream) throws ClosedChannelException {
		SelectionKey key = stream.getChannel().register(reader, SelectionKey.OP_READ, stream);
		try {
			log.info("New client connected: {}", stream.getChannel().getRemoteAddress());
		} catch (IOException e) {
			log.error("Error adding client", e);
		}
		return key;
	}

	public void read(List<ServerWorker> w) {
		try {
			if (reader.selectNow() == 0) {
				// log.debug("Read nothing");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Set<SelectionKey> selectedKeys = reader.selectedKeys();

		Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

		int input;
		SelectionKey key;

		while (keyIterator.hasNext()) {
			key = keyIterator.next();
			if (key.isReadable() && key.isValid()) {
				try {
					input = readWorker(key, w);
					if (input != 0) {
						log.error("EOF reached");
						key.cancel();
						w.add(disconnectAndRemove((ServerNetworkStream) key.attachment()));
					}
				} catch (IOException e) {
					log.error("Error reading from stream", e);
					key.cancel();
					w.add(disconnectAndRemove((ServerNetworkStream) key.attachment()));
				} catch (Exception e) {
					log.error("Error reading from stream", e);
					key.cancel();
					w.add(disconnectAndRemove((ServerNetworkStream) key.attachment()));
				}
			} else {
				log.error("Error with selectionkey");
				key.cancel();
				w.add(disconnectAndRemove((ServerNetworkStream) key.attachment()));
			}
			keyIterator.remove();
		}
	}

	public void write(List<TCP_Packet> wOUT, List<ServerWorker> wIN) {
		for (TCP_Packet packet : wOUT) {
			try {
				int wrote = packet.getNetworkStream().getChannel().write(packet.getDataBuffer());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
