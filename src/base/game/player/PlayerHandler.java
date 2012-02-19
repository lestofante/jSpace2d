package base.game.player;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.HashMap;

import base.game.player.network.NetworkHandler;
import base.worker.Worker;

public class PlayerHandler {
	public NetworkHandler networkHandler;
	private HashMap<String, Player> players = new HashMap<>();
	
	public PlayerHandler() throws IOException{
		this.networkHandler = new NetworkHandler();
	}
	
	public void createPlayer(String name, SelectionKey key){
		players.put(name, new Player(name, key));
	}
	
	public Player getPlayer(String playerName){
		return players.get(playerName);
	}

	public ArrayList<Worker> update() throws IOException {
		ArrayList<Worker> w;
		w = networkHandler.update();
		for (Player p: players.values()){
			p.update();
		}
		
		return w;
	}

}
