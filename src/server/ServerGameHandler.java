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
import base.game.entity.Entity;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP.fromServer.UpdateMapPacket;
import base.game.player.Player;

public class ServerGameHandler {

	private final AtomicInteger turn = new AtomicInteger();

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public final ServerEntityHandlerWrapper entityHandlerWrapper;
	public final ServerPlayerHandlerWrapper playerHandlerWrapper;
	public final ServerNetworkHandler networkHandler;

	private final AsyncActionBus bus;
	private final List<ServerWorker> wIN = new LinkedList<>();
	public final List<TCP_Packet> outgoingPackets = new LinkedList<>();

	private boolean haveToSensSyncPacket = true;

	public ServerGameHandler(AsyncActionBus asyncActionBus) throws IOException {
		this.bus = asyncActionBus;

		this.entityHandlerWrapper = new ServerEntityHandlerWrapper(asyncActionBus, turn);
		this.playerHandlerWrapper = new ServerPlayerHandlerWrapper();

		this.networkHandler = new ServerNetworkHandler();

	}

	public synchronized void update() {
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

		if (turn.get() % 30 == 0)
			sendUpdatePacket();

		networkHandler.write(outgoingPackets, wIN);

		outgoingPackets.clear();
	}

	private void sendUpdatePacket() {
		for (Player p : playerHandlerWrapper.getPlayers()) {
			ServerPlayer player = (ServerPlayer) p;
			UpdateMapPacket packet = new UpdateMapPacket(getVisibleEntities(player), player.getStream());
			outgoingPackets.add(packet);
		}
	}

	private Collection<Entity> getVisibleEntities(ServerPlayer player) {
		return entityHandlerWrapper.getVisibleEntities(player);
	}

	public void setSendSyncPaket(boolean b) {
		haveToSensSyncPacket = b;
	}
}
