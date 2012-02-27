package base.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import base.common.AsyncActionBus;
import base.game.entity.EntityHandler;
import base.game.network.NetworkHandler;
import base.game.player.PlayerHandler;
import base.game.worker.CreateScene;
import base.worker.Worker;

public class GameHandler{
	public EntityHandler entityHandler;
	public PlayerHandler playerHandler;
	public final AtomicBoolean run = new AtomicBoolean();
	public final AtomicInteger step=new AtomicInteger();
	protected NetworkHandler networkHandler;
	public ArrayList<Worker> wOUT = new ArrayList<>();
	

	public void update() {
		ArrayList<Worker> wIN = new ArrayList<>();
		
		try {
			networkHandler.read(wIN);
			
			for (Worker wTmp:wIN){
				wTmp.update(this);
			}
			wIN.clear();
			playerHandler.update(wIN);
			entityHandler.update(wIN);
			
			for (Worker wTmp:wIN){
				wTmp.update(this);
			}
			
			networkHandler.write(wOUT);
			wOUT.clear();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		
		
	}

}
