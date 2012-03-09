package server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.net.worker.Login;
import base.game.network.packets.PacketHandler;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP_Packet.TCP_PacketType;
import base.game.network.packets.TCP.LoginPacket;
import base.worker.Worker;

public class LoginHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final ServerSocketChannel listenerChannel;
	private final HashMap<SocketChannel, Long> pendingConnections = new HashMap<>();
	private final static long connectionTimeout = 10000;

	public LoginHandler(int port) throws IOException {
		listenerChannel = ServerSocketChannel.open();
		listenerChannel.configureBlocking(false);
		listenerChannel.bind(new InetSocketAddress(port));
		log.info("Server started listening on port {}", port);
	}

	public void checkForLoginRequests(ArrayList<Worker> w) throws IOException {
		Login wLogin = null;

		if (!pendingConnections.isEmpty()) {
			Iterator<Entry<SocketChannel, Long>> entrySetIterator = pendingConnections.entrySet().iterator();

			while (entrySetIterator.hasNext()) {
				Entry<SocketChannel, Long> entry = entrySetIterator.next();
				SocketAddress address = entry.getKey().getRemoteAddress();
				if (System.currentTimeMillis() - entry.getValue() > connectionTimeout) {
					try {
						throw new Exception();
					} catch (Exception e) {
						log.error("No login arrived before timeout from {}", address, e);
					}
					entry.getKey().close();
					pendingConnections.remove(entry.getKey());
					log.debug("Removed pending connection: {}", address);
					continue;
				}
				ByteBuffer dst = ByteBuffer.allocate(32);
				dst.clear();

				int bytesRead = entry.getKey().read(dst);

				if (bytesRead <= 0) {
					continue;
				}

				log.debug("{} bytes read from {} ", bytesRead, entry.getKey().getRemoteAddress());

				dst.flip();
				TCP_Packet packet = null;

				try {
					packet = PacketHandler.getLogin(dst);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (packet != null) {
					if (packet.PacketType == TCP_PacketType.LOGIN) {
						if (dst.hasRemaining()) {
							// Are you trying to inject? NO PACKETS BEFORE
							// LOGIN! Son I am disappoint...
							ditchPendingConnection(entry);
							try {
								throw new Exception();
							} catch (Exception e) {
								log.error("Packet arrived before login was acknowledged", e);
							}
						}
						wLogin = new Login((LoginPacket) packet, entry.getKey());
						log.debug("Received a login from address: {} with username: {}", entry.getKey().getRemoteAddress(), ((LoginPacket) packet).getUsername());
						w.add(wLogin);
						pendingConnections.remove(entry.getKey());
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

	private void ditchPendingConnection(Entry<SocketChannel, Long> entry) throws IOException {
		entry.getKey().close();
		pendingConnections.remove(entry.getKey());
	}

	public void acceptNewConnection() throws IOException {
		SocketChannel accept = listenerChannel.accept();
		if (accept != null) {
			log.info("Connection initiated by: {}", accept.getRemoteAddress());
			accept.configureBlocking(false);
			pendingConnections.put(accept, System.currentTimeMillis());
		}
	}

}
