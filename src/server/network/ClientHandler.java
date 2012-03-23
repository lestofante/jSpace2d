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

import server.network.worker.S_RemoveNetworkPlayer;
import server.worker.ServerWorker;
import base.game.network.NetworkStream;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP_Packet.TCP_PacketType;

public class ClientHandler {

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
		return new S_RemoveNetworkPlayer(stream);
	}

	private ServerWorker readWorker(SelectionKey key) throws Exception {
		NetworkStream stream = (NetworkStream) key.attachment();
		stream.update();

		while (!stream.available.isEmpty()) {
			TCP_Packet packet = stream.available.remove();

			if (!packet.PacketType.equals(TCP_PacketType.LOGIN)) {

			} else {
				throw new Exception("Address already logged in");
			}
		}

		return null;

	}

	public SelectionKey addConnectedClient(ServerNetworkStream stream) throws ClosedChannelException {
		SelectionKey key = stream.in.register(reader, SelectionKey.OP_READ, stream);
		try {
			log.info("New client connected: {}", stream.in.getRemoteAddress());
		} catch (IOException e) {
			log.error("Error adding client", e);
		}
		return key;
	}

	public void read(List<ServerWorker> w) {
		try {
			reader.selectNow();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Set<SelectionKey> selectedKeys = reader.selectedKeys();

		Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

		ServerWorker input;
		SelectionKey key;

		while (keyIterator.hasNext()) {
			key = keyIterator.next();
			if (key.isReadable() && key.isValid()) {
				try {
					input = readWorker(key);
					if (input != null)
						w.add(input);
				} catch (IOException e) {
					log.error("Error reading from stream", e);
					key.cancel();
					w.add(disconnectAndRemove((ServerNetworkStream) key.attachment()));
				} catch (Exception e) {
					log.error("Error reading from stream", e);
					w.add(disconnectAndRemove((ServerNetworkStream) key.attachment()));
					key.cancel();
				}
			}
			keyIterator.remove();
		}
	}

	public void write(List<TCP_Packet> wOUT, List<ServerWorker> wIN) {
		for (TCP_Packet packet : wOUT) {
			try {
				packet.getNetworkStream().in.write(packet.getDataBuffer());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
