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
			e.printStackTrace();
		}

		LoginPacket lPacket = new LoginPacket("falskd123asdfasdf", (byte) 0);

		SocketChannel kkSocket = null;

		try {
			kkSocket = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true)
			try {
				send(lPacket, kkSocket);
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	private void send(LoginPacket lPacket, SocketChannel kkSocket) {

		try {
			System.out.println("Wrote " + kkSocket.write(lPacket.getDataBuffer()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {

	}

}
