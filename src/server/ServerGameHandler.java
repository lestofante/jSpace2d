package server;

import java.io.IOException;

import server.entity.ServerEntityHandler;
import server.net.ServerNetworkHandler;
import server.player.ServerPlayerHandler;

import base.common.AsyncActionBus;
import base.game.GameHandler;
import base.game.worker.CreateScene;

public class ServerGameHandler extends GameHandler{

	public ServerGameHandler(AsyncActionBus asyncActionBus) {
		this.entityHandler = new ServerEntityHandler(asyncActionBus, step);
		
		try {
			this.playerHandler = new ServerPlayerHandler();
			this.networkHandler = new ServerNetworkHandler();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		
		createScene("simpleWalls.xml");
	}
	
	private void createScene(String mapName) {
		new CreateScene(mapName).update(this);
	}

}
