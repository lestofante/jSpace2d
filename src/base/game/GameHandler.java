package base.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import base.common.AsyncActionBus;
import base.game.entity.EntityHandler;
import base.game.player.PlayerHandler;
import base.game.worker.CreateScene;
import base.worker.Worker;

public class GameHandler{
	public EntityHandler entityHandler;
	public PlayerHandler playerHandler;
	public final AtomicBoolean run = new AtomicBoolean();
	public final AtomicInteger step=new AtomicInteger();
	
	public GameHandler(AsyncActionBus asyncActionBus) {
		this.entityHandler = new EntityHandler(asyncActionBus, step);
		try {
			this.playerHandler = new PlayerHandler();
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

	public void update() {
		ArrayList<Worker> w = new ArrayList<>();
		try {
	
			w.addAll( playerHandler.update() );
			w.addAll( entityHandler.update() );
			
			for (Worker wTmp:w){
				wTmp.update(this);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		
		
	}

}
