package client;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.common.AsyncActionBus;
import base.game.network.NetworkStream;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP.fromServer.SynchronizeMapPacket;
import client.entity.EntityHandlerClientWrapper;
import client.network.ClientNetworkHandler;
import client.player.PlayerHandlerClientWrapper;
import client.worker.ClientWorker;
import client.worker.SynchronizeMap;

public class ClientGameHandler {
	/*
	 * Tools
	 */
	public final PlayerHandlerClientWrapper playerHandlerClientWrapper;
	public final EntityHandlerClientWrapper entityHandlerClientWrapper;
	public final ClientNetworkHandler networkHandler;
	public final UpdateManager updateManager;

	private final String myName;
	private final InputManager inputManager;
	private final AsyncActionBus bus;
	private final AtomicInteger turn = new AtomicInteger();
	private final List<ClientWorker> wIN = new LinkedList<>();
	private final List<TCP_Packet> wOUT = new LinkedList<>();
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final long timeStep = 12500000;

	private long timeBuffer;

	private long lastCheck;

	private long pUpdates;

	private long deltaStart;

	private long sleepTime;

	public ClientGameHandler(AsyncActionBus bus, String clientName, NetworkStream toServer, SynchronizeMapPacket sP) throws IOException {
		this.bus = bus;
		this.myName = clientName;

		while (!bus.graphicsStarted.get()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		this.inputManager = new InputManager(bus);
		this.updateManager = new UpdateManager();
		this.networkHandler = new ClientNetworkHandler(toServer);
		this.playerHandlerClientWrapper = new PlayerHandlerClientWrapper(myName);
		this.entityHandlerClientWrapper = new EntityHandlerClientWrapper(bus, turn, timeStep);
		wIN.add(new SynchronizeMap(sP));
	}

	public void update() throws IOException {
		if (getDelta() + timeBuffer > timeStep) {
			timeBuffer += getDelta();
			deltaStart = System.nanoTime();

			while (timeBuffer > timeStep) {
				networkHandler.read(wIN);
				inputManager.update(wIN);
				updateManager.update(this);

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

				wOUT.clear();
				timeBuffer -= timeStep;
				pUpdates++;
				updatePPS();
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

	private long getDelta() {
		return System.nanoTime() - deltaStart;
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

	public void sendToServer(TCP_Packet toSend) {
		wOUT.add(toSend);
	}

	public void start() {
		deltaStart = System.nanoTime();
	}
}
