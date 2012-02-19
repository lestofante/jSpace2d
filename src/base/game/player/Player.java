package base.game.player;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;

import base.game.entity.Entity;
import base.worker.NetworkWorker;

public class Player {
	
	public final String playerName;
	public final SelectionKey connectionKey;
	public Entity currentEntity;
	public ArrayList<NetworkWorker> output = new ArrayList<>();
	
	public Player(String playerName, SelectionKey connectionKey){
		this.playerName = playerName;
		this.connectionKey = connectionKey;		
	}

	public void update() {

	}
	
}
