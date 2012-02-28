package client;

import java.io.IOException;

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

	}

	@Override
	public void update() {

	}

}
