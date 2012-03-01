package base.game;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import base.game.entity.EntityHandler;
import base.game.network.NetworkHandler;
import base.game.player.PlayerHandler;
import base.worker.Worker;

public abstract class GameHandler {
	public EntityHandler entityHandler;
	public PlayerHandler playerHandler;
	public final AtomicBoolean run = new AtomicBoolean();
	public final AtomicInteger step = new AtomicInteger();
	public NetworkHandler networkHandler;
	public ArrayList<Worker> wOUT = new ArrayList<>();
	public ArrayList<Worker> wIN = new ArrayList<>();;

	public abstract void update();

}
