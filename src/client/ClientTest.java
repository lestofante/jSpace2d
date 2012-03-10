package client;

import org.lwjgl.opengl.DisplayMode;

import base.common.AsyncActionBus;
import base.graphics.GraphicsManager;

public class ClientTest implements Runnable {

	/**
	 * @param args
	 */

	AsyncActionBus bus = new AsyncActionBus();
	ClientGameHandler g;
	GraphicsManager gr = new GraphicsManager(new DisplayMode(800, 800), true, true, bus);;
	private final String playerName;
	private final String serverAddress;
	private final int serverPort;

	public ClientTest(String playerName, String serverAddress, int serverPort) {
		this.playerName = playerName;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}
	
	
	@Override
	public void run() {
		Thread.currentThread().setName("Client " + playerName);
		
		Thread graphicsThread = new Thread(gr);
		graphicsThread.setName("Client Graphics");
		graphicsThread.start();
		
		//da qui in poi la grafica è per conto suo
		
		g = new ClientGameHandler(bus, playerName, serverAddress, serverPort);
		
		//e qui c'è il loop di gioco (le varie wait sono dentro update)
		while (true) {
			g.update();
		}
	}
}
