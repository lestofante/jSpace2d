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
import base.game.network.packets.TCP.fromServer.SynchronizeMapPacket;
import base.game.network.packets.TCP.fromServer.UpdateMapPacket;
import base.game.network.packets.TCP.toServer.LoginPacket;
import base.game.network.packets.TCP.toServer.RequestEntityPacket;
import base.graphics.GraphicsManager;

public class ClientTest implements Runnable {

	private static final long MAX_WAIT = 10000; // in milliseconds
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

	private SynchronizeMapPacket sP;
	private UpdateMapPacket uP;

	public ClientTest(String playerName, String serverAddress, int serverPort) {
		this.playerName = playerName;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("Client " + playerName);

		try {
			NetworkStream toServer = connect(System.currentTimeMillis());
			if (toServer == null) {
				log.error("Couldn't connecto to server");
				System.exit(-1);
			}

			log.debug("Connection to server succeded");

			if (!getGameInformation(toServer)) {
				log.error("Could not retrieve game information from server");
				System.exit(-1);
			}

			log.debug("Successful information from server gathered. Starting game");
			Thread graphicsThread = new Thread(gr);
			graphicsThread.setName("Client Graphics");
			graphicsThread.start();
			g = new ClientGameHandler(bus, playerName, toServer, sP);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g.start();
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

	private boolean getGameInformation(NetworkStream toServer) {
		try {
			toServer.getChannel().write(new RequestEntityPacket(toServer, 0).getDataBuffer());
			TCP_Packet shouldBeSyncMapPacket = null;

			long start = System.currentTimeMillis();

			while (System.currentTimeMillis() - start < MAX_WAIT) {
				toServer.update();
				shouldBeSyncMapPacket = toServer.available.poll();
				if (shouldBeSyncMapPacket != null)
					break;
			}

			if (shouldBeSyncMapPacket == null) {
				log.error("Failed to retrieve syncPacket");
				return false;
			}
			if (!shouldBeSyncMapPacket.PacketType.equals(TCP_PacketType.SYNC_MAP)) {
				log.error("Received {} instead of SyncPacket", shouldBeSyncMapPacket.PacketType.name());
				return false;
			}

			sP = (SynchronizeMapPacket) shouldBeSyncMapPacket;

			log.debug("Received first synchronization packet");

			TCP_Packet shouldBeUpdateMapPacket = null;

			while (System.currentTimeMillis() - start < MAX_WAIT) {
				toServer.update();
				shouldBeUpdateMapPacket = toServer.available.poll();
				if (shouldBeUpdateMapPacket != null)
					break;
			}

			if (shouldBeUpdateMapPacket == null)
				return false;
			if (!shouldBeUpdateMapPacket.PacketType.equals(TCP_PacketType.UPDATE_MAP))
				return false;

			log.debug("OK till here");

			uP = (UpdateMapPacket) shouldBeUpdateMapPacket;

			log.debug("Received first map update");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
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
			channel.socket().setTcpNoDelay(true);
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

		while (System.currentTimeMillis() - start < MAX_WAIT) {
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
