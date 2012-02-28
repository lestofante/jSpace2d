package client;

import java.io.IOException;

import base.common.AsyncActionBus;
import base.game.GameHandler;

public class ClientGameHandler extends GameHandler {

	public ClientGameHandler(AsyncActionBus bus) {
		try {
			this.networkHandler = new ClientNetworkHandler();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		
	}

}
