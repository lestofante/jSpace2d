package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import org.lwjgl.opengl.DisplayMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.common.AsyncActionBus;
import base.game.network.NetworkStream;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP_Packet.TCP_PacketType;
import base.game.network.packets.TCP.toServer.LoginPacket;
import base.game.network.packets.TCP.toServer.RequestEntity;
import base.graphics.GraphicsManager;

public class ClientTest implements Runnable {

	/**
	 * @param args
	 */

	private final AsyncActionBus bus = new AsyncActionBus();
	private ClientGameHandler g;
	private final GraphicsManager gr = new GraphicsManager(new DisplayMode(800, 800), true, true, bus);;
	private final String playerName;
	private final String serverAddress;
	private final int serverPort;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public ClientTest(String playerName, String serverAddress, int serverPort) {
		this.playerName = playerName;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("Client " + playerName);

		Thread graphicsThread = new Thread(gr);
		graphicsThread.setName("Client Graphics");
		graphicsThread.start();

		// da qui in poi la grafica e per conto suo

		try {
			NetworkStream toServer = connect(System.currentTimeMillis());
			if (toServer == null) {
				log.error("no UpdateMap received in time, exiting");
				System.exit(-1);
			}
			//TCP_Packet myself = null;
			//request type of game
			//myself = 
			requestEntity(toServer);
			g = new ClientGameHandler(bus, playerName, toServer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// e qui c'e il loop di gioco (le varie wait sono dentro update)
		while (true) {
			try {
				g.update();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private TCP_Packet requestEntity(NetworkStream toServer) {
		try {
			toServer.getChannel().write(new RequestEntity(toServer, 1).getDataBuffer());
/*			TCP_Packet shouldBeUpdateMapPacket = null;

			long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start < 10000) {
				toServer.update();
				shouldBeUpdateMapPacket = toServer.available.poll();
				if (shouldBeUpdateMapPacket != null)
					break;
			}

			if (shouldBeUpdateMapPacket != null)
				if (shouldBeUpdateMapPacket.PacketType.equals(TCP_PacketType.UPDATE_MAP))
					return shouldBeUpdateMapPacket;
*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private NetworkStream connect(long start) throws Exception {

		SocketChannel channel = null;
		try {
			channel = SocketChannel.open(new InetSocketAddress(serverAddress, serverPort));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NetworkStream stream = null;

		if (channel != null) {
			channel.configureBlocking(false);
			stream = new NetworkStream(channel);
			try {
				channel.write(new LoginPacket(playerName, stream).getDataBuffer());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			log.error("Error opening channel");
			System.exit(-1);
		}

		TCP_Packet shouldBeSyncMapPacket = null;

		while (System.currentTimeMillis() - start < 10000) {
			stream.update();
			shouldBeSyncMapPacket = stream.available.poll();
			if (shouldBeSyncMapPacket != null)
				break;
		}

		if (shouldBeSyncMapPacket != null)
			if (shouldBeSyncMapPacket.PacketType.equals(TCP_PacketType.SYNC_MAP))
				return stream;

		return null;
	}
}
