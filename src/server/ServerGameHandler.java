package server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import server.entity.ServerEntityHandlerWrapper;
import server.network.ServerNetworkHandler;
import server.player.ServerPlayerHandlerWrapper;
import server.worker.ServerWorker;
import base.common.AsyncActionBus;
import base.game.network.packets.TCP_Packet;

public class ServerGameHandler {

	private final AtomicInteger turn = new AtomicInteger();

	public final ServerEntityHandlerWrapper entityHandlerWrapper;
	public final ServerPlayerHandlerWrapper playerHandlerWrapper;
	public final ServerNetworkHandler networkHandler;

	private final AsyncActionBus bus;
	private final List<ServerWorker> wIN = new LinkedList<>();
	public final List<TCP_Packet> outgoingPackets = new LinkedList<>();

	public ServerGameHandler(AsyncActionBus asyncActionBus) throws IOException {
		this.bus = asyncActionBus;

		this.entityHandlerWrapper = new ServerEntityHandlerWrapper(asyncActionBus, turn);
		this.playerHandlerWrapper = new ServerPlayerHandlerWrapper();

		this.networkHandler = new ServerNetworkHandler();

	}

	public void update() {

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

		networkHandler.write(outgoingPackets, wIN);
	}

}
