package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import base.common.AsyncActionBus;
import base.game.GameHandler;
import base.game.network.packets.LoginPacket;

public class ClientGameHandler extends GameHandler {

	public ClientGameHandler(AsyncActionBus bus) {
		try {
			this.networkHandler = new ClientNetworkHandler();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LoginPacket lPacket = new LoginPacket("maronna", (byte) 0);

		SocketChannel kkSocket = null;
		try {
			kkSocket = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			System.out.println("Wrote " + kkSocket.write(lPacket.getDataBuffer()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update() {

	}

}
