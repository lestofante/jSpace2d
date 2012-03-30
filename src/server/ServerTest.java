package server;

import java.io.IOException;

import base.common.AsyncActionBus;

public class ServerTest {

	/**
	 * @param args
	 */

	AsyncActionBus bus = new AsyncActionBus();
	private ServerGameHandler g;

	// GraphicsManager gr = new GraphicsManager(new DisplayMode(800, 800), true,
	// true, bus);

	public static void main(String[] args) {
		Thread.currentThread().setName("Server");
		new ServerTest();
	}

	public ServerTest() {
		g = null;

		try {
			g = new ServerGameHandler(bus);
		} catch (IOException e) {

			e.printStackTrace();
			System.exit(-1);
		}

		// Thread graphicsThread = new Thread(gr);
		// graphicsThread.setName("Server Graphics");
		// graphicsThread.start();
		g.start();
		while (true) {
			g.update();
		}
	}
}
