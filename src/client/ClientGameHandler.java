package client;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import base.common.AsyncActionBus;
import base.game.network.NetworkStream;
import base.game.network.packets.TCP_Packet;
import client.entity.EntityHandlerClientWrapper;
import client.player.PlayerHandlerClientWrapper;
import client.worker.ClientWorker;

public class ClientGameHandler {
	/*
	 * Tools
	 */
	public final PlayerHandlerClientWrapper playerHandlerClientWrapper;
	public final EntityHandlerClientWrapper entityHandlerClientWrapper;
	public final ClientNetworkHandler networkHandler;

	private final String myName;
	private final InputManager inputManager;
	private final AsyncActionBus bus;
	private final AtomicInteger turn = new AtomicInteger();
	private final List<ClientWorker> wIN = new LinkedList<>();
	private final List<TCP_Packet> wOUT = new LinkedList<>();

	public ClientGameHandler(AsyncActionBus bus, String clientName, NetworkStream toServer) throws IOException {
		this.bus = bus;
		this.myName = clientName;

		this.inputManager = new InputManager(myName);
		this.networkHandler = new ClientNetworkHandler(toServer);
		this.playerHandlerClientWrapper = new PlayerHandlerClientWrapper(myName);
		this.entityHandlerClientWrapper = new EntityHandlerClientWrapper(bus, turn);
	}

	public void update() throws IOException {

		networkHandler.read(wIN);
		inputManager.update(wIN);

		for (ClientWorker wTmp : wIN) {
			wTmp.execute(this);
		}

		wIN.clear();

		playerHandlerClientWrapper.update(wIN);
		entityHandlerClientWrapper.update(wIN);

		for (ClientWorker wTmp : wIN) {
			wTmp.execute(this);
		}

		wIN.clear();

		networkHandler.write(wOUT, wIN);

	}

}
