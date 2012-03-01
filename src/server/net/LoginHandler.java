package server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
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
import base.game.network.packets.LoginPacket;
import base.game.network.packets.PacketRecognizer;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP_Packet.PacketType;
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
	}

	public void checkForLoginRequests(ArrayList<Worker> w) throws IOException {
		Login wLogin = null;

		if (!pendingConnections.isEmpty()) {
			Iterator<Entry<SocketChannel, Long>> entrySetIterator = pendingConnections.entrySet().iterator();

			while (entrySetIterator.hasNext()) {
				Entry<SocketChannel, Long> entry = entrySetIterator.next();
				if (System.currentTimeMillis() - entry.getValue() > connectionTimeout) {
					try {
						throw new Exception();
					} catch (Exception e) {
						log.error("No login arrived before timeout from {}", entry.getKey().getRemoteAddress(), e);
					}
					entry.getKey().close();
					pendingConnections.remove(entry.getKey());
					continue;
				}
				ByteBuffer dst = ByteBuffer.allocate(32);
				dst.clear();
				int bytesRead = entry.getKey().read(dst);

				if (bytesRead <= 0) {
					continue;
				}

				dst.flip();
				TCP_Packet packet = null;

				try {
					packet = PacketRecognizer.getTCP(dst);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (packet != null) {
					if (packet.packetType == PacketType.LOGIN) {
						wLogin = new Login((LoginPacket) packet);
						wLogin.setChannel(entry.getKey());
						log.debug("Received a login from address: {} with username: {}", entry.getKey().getRemoteAddress(), ((LoginPacket) packet).getUsername());
						w.add(wLogin);
						pendingConnections.remove(entry.getKey());
					} else {
						entry.getKey().close();
						pendingConnections.remove(entry.getKey());
						try {
							throw new Exception();
						} catch (Exception e) {
							log.error("Not a login packet", e);
						}
					}
				} else {
					entry.getKey().close();
					pendingConnections.remove(entry.getKey());
					try {
						throw new Exception();
					} catch (Exception e) {
						log.error("Error handling packet", e);
					}
				}
			}
		}
	}

	// va bene ora? uhmm non capisco perchè vuoi troware l'eccezzione....
	/*
	 * per chiarezza ma andebbe nel logger, sicuramente non è chiarezza stampare
	 * lo stack trace, visto che fa riferimento a quì :-) guarda se hai voglia
	 * di implementare il logger :) sicuramente così è meglio che stampare in
	 * stdout, e se non lo fai qui, no? certo stampi in stderr :-) giusto :) mi
	 * porti in tou a vedere il PacketRecognizer? si
	 */

	public void acceptNewConnection() throws IOException {
		SocketChannel accept = listenerChannel.accept();
		if (accept != null) {
			log.info("Connection initiated by: {}", accept.getRemoteAddress());
			accept.configureBlocking(false);
			pendingConnections.put(accept, System.currentTimeMillis());
		}
	}

}
