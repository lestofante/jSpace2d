package base.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.entity.EntityHandler;
import base.game.network.NetworkHandler;
import base.game.network.packets.TCP_Packet;
import base.game.player.NetworkPlayer;
import base.game.player.Player;
import base.game.player.PlayerHandler;
import base.game.worker.LoadMap;
import base.worker.Worker;

public abstract class GameHandler {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	public EntityHandler entityHandler;
	public PlayerHandler playerHandler;
	public final AtomicBoolean run = new AtomicBoolean();
	public final AtomicInteger step = new AtomicInteger();
	public NetworkHandler networkHandler;
	public HashMap<NetworkPlayer, ArrayList<TCP_Packet>> wOUT = new HashMap<>();
	public ArrayList<Worker> wIN = new ArrayList<>();;

	public abstract void update();

	public void loadMap(String string) {
		LoadMap lMWorker = new LoadMap(string);
		wIN.add(lMWorker);
	}

}
