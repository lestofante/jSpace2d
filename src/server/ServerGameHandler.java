package server;

import java.io.IOException;

import server.entity.ServerEntityHandler;
import server.net.ServerNetworkHandler;
import server.player.ServerPlayerHandler;
import base.common.AsyncActionBus;
import base.game.GameHandler;
import base.worker.Worker;

public class ServerGameHandler extends GameHandler {

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
	}

	@Override
	public void update() {
		

		try {
			networkHandler.read(wIN);

			for (Worker wTmp : wIN) {
				wTmp.execute(this);
			}
			
			wIN.clear();
			playerHandler.update(wIN);
			entityHandler.update(wIN);

			for (Worker wTmp : wIN) {
				wTmp.execute(this);
			}
			wIN.clear();
			
			networkHandler.write(wOUT,wIN);
			wOUT.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
