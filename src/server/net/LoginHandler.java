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

import base.game.network.Login;
import base.game.network.packets.LoginPacket;
import base.game.network.packets.PacketRecognizer;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP_Packet.PacketType;
import base.worker.Worker;

public class LoginHandler {

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
						System.out.println("Received a login from address: " + entry.getKey().getRemoteAddress() + " with username: " + ((LoginPacket) packet).getUsername());
						w.add(wLogin);
						pendingConnections.remove(entry.getKey());
					} else {
						entry.getKey().close();
						pendingConnections.remove(entry.getKey());
						try {
							throw new Exception("Not a login packet");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					entry.getKey().close();
					pendingConnections.remove(entry.getKey());
					try {
						throw new Exception("Error handling packet");
					} catch (Exception e) {
						e.printStackTrace();
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
			accept.configureBlocking(false);
			pendingConnections.put(accept, System.currentTimeMillis());
		}
	}

}
