package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import base.common.AsyncActionBus;
import base.game.GameHandler;
import base.game.network.packets.PacketRecognizer;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP_Packet.TCP_PacketType;
import base.game.network.packets.TCP.LoginPacket;

public class ClientGameHandler extends GameHandler {

	public ClientGameHandler(AsyncActionBus bus, String clientName, String serverAddress, int serverPort) {
		try {
			this.networkHandler = new ClientNetworkHandler();
		} catch (IOException e) {
			e.printStackTrace();
		}

		SocketChannel kkSocket = null;

		try {
			log.debug("Opening connection with {}:{}", serverAddress, serverPort);
			kkSocket = SocketChannel.open(new InetSocketAddress(serverAddress, serverPort));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			log.debug("Opened connection with {}", kkSocket.getRemoteAddress());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		LoginPacket lPacket = new LoginPacket(clientName);

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			log.error("Error with thread sleep", e);
		}

		send(lPacket, kkSocket);

		try {
			kkSocket.configureBlocking(false);
		} catch (IOException e) {
			log.error("Error blocking channel", e);
		}

		while (true)
			try {
				ByteBuffer buf = ByteBuffer.allocate(1500);
				int read = kkSocket.read(buf);
				if (read != -1) {
					buf.flip();
					if (buf.hasRemaining()) {
						log.debug("Read {} bytes in {}", read, buf);
						TCP_Packet in = PacketRecognizer.getTCP(buf);
						if (in.PacketType == TCP_PacketType.UPDATE_MAP) {
							log.info("UPDATE MAP PACKET!\n{}", in);
						}
					}
					log.info("Still connected");
				} else {
					log.error("EOF: lost connection to server");
				}
				Thread.sleep(200);
			} catch (InterruptedException e) {
				log.error("Error with thread sleep", e);
			} catch (IOException e) {
				log.error("Error with connection", e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	private void send(LoginPacket lPacket, SocketChannel kkSocket) {

		try {
			log.debug("Wrote {} bytes", kkSocket.write(lPacket.getDataBuffer()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {

	}

}
