package server.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.network.worker.Login;
import server.worker.ServerWorker;
import base.game.network.NetworkStream;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP_Packet.TCP_PacketType;
import base.game.network.packets.TCP.LoginPacket;

public class LoginHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final ServerSocketChannel listenerChannel;
	private final HashMap<NetworkStream, Long> pendingConnections = new HashMap<>();
	private final static long connectionTimeout = 10000;

	public LoginHandler(int port) throws IOException {
		listenerChannel = ServerSocketChannel.open();
		listenerChannel.configureBlocking(false);
		listenerChannel.bind(new InetSocketAddress(port));
		log.info("Server started listening on port {}", port);
	}

	public void checkForLoginRequests(List<ServerWorker> w) throws IOException {
		ServerWorker wLogin = null;
		if (!pendingConnections.isEmpty()) {
			Iterator<Entry<NetworkStream, Long>> entrySetIterator = pendingConnections.entrySet().iterator();

			while (entrySetIterator.hasNext()) {
				Entry<NetworkStream, Long> entry = entrySetIterator.next();
				NetworkStream stream = entry.getKey();
				SocketAddress address = stream.getChannel().getRemoteAddress();
				if (System.currentTimeMillis() - entry.getValue() > connectionTimeout) {
					try {
						throw new Exception();
					} catch (Exception e) {
						log.error("No login arrived before timeout from {}", address, e);
					}
					ditchPendingConnection(entry);
					log.debug("Removed pending connection: {}", address);
					continue;
				}

				stream.update();

				TCP_Packet packet = stream.available.poll();

				if (packet != null) {
					if (packet.PacketType == TCP_PacketType.LOGIN) {
						if (!stream.available.isEmpty()) {
							// Are you trying to inject? NO PACKETS BEFORE
							// LOGIN! Son I am disappoint...
							ditchPendingConnection(entry);
							try {
								throw new Exception();
							} catch (Exception e) {
								log.error("Packet arrived before login was acknowledged", e);
							}
						}
						wLogin = new Login((LoginPacket) packet, stream);
						log.debug("Received a login from address: {} with username: {}", address, ((LoginPacket) packet).getUsername());

						w.add(wLogin);
						pendingConnections.remove(stream);
					} else {
						ditchPendingConnection(entry);
						try {
							throw new Exception();
						} catch (Exception e) {
							log.error("Not a login packet", e);
						}
					}
				}
			}
		}
	}

	private void ditchPendingConnection(Entry<NetworkStream, Long> entry) throws IOException {
		entry.getKey().getChannel().close();
		pendingConnections.remove(entry.getKey());
	}

	public void acceptNewConnection() throws IOException {
		SocketChannel accept = listenerChannel.accept();
		if (accept != null) {
			log.info("Connection initiated by: {}", accept.getRemoteAddress());
			accept.configureBlocking(false);
			NetworkStream stream = null;
			try {
				stream = new NetworkStream(accept);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			pendingConnections.put(stream, System.currentTimeMillis());
		}
	}

}
