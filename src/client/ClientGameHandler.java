package client;

import java.io.IOException;

import server.entity.ServerEntityHandler;

import base.common.AsyncActionBus;
import base.game.GameHandler;
import base.worker.Worker;

public class ClientGameHandler extends GameHandler {

	private String myName;
	InputHandler inputHandler;

	public ClientGameHandler(AsyncActionBus bus, String clientName, String serverAddress, int serverPort) {
		myName = clientName;
		inputHandler = new InputHandler(myName);
		this.entityHandler = new ServerEntityHandler(bus, step);

		try {
			this.playerHandler = new ClientPlayerHandler();
			this.networkHandler = new ClientNetworkHandler();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public void update() {
		
		networkHandler.read(wIN);
		
		inputHandler.update(wIN);
		
		for (Worker wTmp : wIN) {
			wTmp.execute(this);
		}

		wIN.clear();		
		
		//playerHandler.update(wIN);
		//entityHandler.update(wIN);

		for (Worker wTmp : wIN) {
			wTmp.execute(this);
		}
		wIN.clear();

		networkHandler.write(wOUT, wIN);
	}

}
