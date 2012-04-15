package server;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.entity.ServerEntityHandlerWrapper;
import server.network.ServerNetworkHandler;
import server.network.worker.SyncPlayers;
import server.player.ServerPlayer;
import server.player.ServerPlayerHandlerWrapper;
import server.worker.ServerWorker;
import base.common.AsyncActionBus;
import base.common.Constants;
import base.game.entity.Entity;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP.fromServer.UpdateMapPacket;
import base.game.player.Player;

public class ServerGameHandler {

	private static final long timeStep = 12500000;

	private final AtomicInteger turn = new AtomicInteger();

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public final ServerEntityHandlerWrapper entityHandlerWrapper;
	public final ServerPlayerHandlerWrapper playerHandlerWrapper;
	public final ServerNetworkHandler networkHandler;

	private final AsyncActionBus bus;
	private final List<ServerWorker> wIN = new LinkedList<>();
	public final List<TCP_Packet> outgoingPackets = new LinkedList<>();

	private boolean haveToSensSyncPacket = true;

	private long timeBuffer;

	private long lastCheck;

	private long pUpdates;

	private long deltaStart;

	private long sleepTime;

	public ServerGameHandler(AsyncActionBus asyncActionBus) throws IOException {
		this.bus = asyncActionBus;

		this.entityHandlerWrapper = new ServerEntityHandlerWrapper(bus, turn, timeStep);
		this.playerHandlerWrapper = new ServerPlayerHandlerWrapper();

		this.networkHandler = new ServerNetworkHandler();
	}

	public void update() {
		if (getDelta() + timeBuffer > timeStep) {
			timeBuffer += getDelta();
			deltaStart = System.nanoTime();

			while (timeBuffer > timeStep) {
				networkHandler.read(wIN);

				for (ServerWorker wTmp : wIN) {
					wTmp.execute(this);
				}

				wIN.clear();
				playerHandlerWrapper.update(wIN);
				entityHandlerWrapper.update(wIN);

				for (ServerWorker wTmp : wIN) {
					wTmp.execute(this);
				}
				wIN.clear();

				if (haveToSensSyncPacket) {
					haveToSensSyncPacket = false;
					SyncPlayers sP = new SyncPlayers();
					sP.execute(this);
				}

				if (turn.get() % Constants.UPDATE_FREQ == 0)
					sendUpdatePacket();

				networkHandler.write(outgoingPackets, wIN);

				outgoingPackets.clear();

				turn.getAndIncrement();
				pUpdates++;
				updatePPS();
				timeBuffer -= timeStep;
			}

		} else {
			long toNextUpdate = (timeStep - getDelta()) / 1000000;
			if (toNextUpdate > 2)
				try {
					sleepTime += toNextUpdate;
					Thread.sleep(toNextUpdate);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public void start() {
		deltaStart = System.nanoTime();
	}

	public void stop() {

	}

	private void updatePPS() {
		long deltaPhysics = System.nanoTime() - lastCheck;
		if (deltaPhysics > 1000000000) {
			lastCheck = System.nanoTime();
			deltaPhysics /= 1000000000;
			log.info("pps: {}", pUpdates / deltaPhysics);
			log.info("total sleep time: {} milliseconds", sleepTime);
			sleepTime = 0;
			pUpdates = 0;
		}
	}

	private long getDelta() {
		return System.nanoTime() - deltaStart;
	}

	private void sendUpdatePacket() {
		for (Player p : playerHandlerWrapper.getPlayersValues()) {
			ServerPlayer player = (ServerPlayer) p;
			if (player.isPlaying()) {
				UpdateMapPacket packet = new UpdateMapPacket(getVisibleEntities(player), player.getStream(), System.nanoTime());
				outgoingPackets.add(packet);
			}
		}
	}

	private Collection<Entity> getVisibleEntities(ServerPlayer player) {
		return entityHandlerWrapper.getVisibleEntities(player);
	}

	public void setSendSyncPaket(boolean b) {
		haveToSensSyncPacket = b;
	}
}
