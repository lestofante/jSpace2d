package server;

import org.lwjgl.opengl.DisplayMode;

import base.common.AsyncActionBus;
import base.graphics.GraphicsManager;

public class ServerTest {

	/**
	 * @param args
	 */

	AsyncActionBus bus = new AsyncActionBus();
	ServerGameHandler g = new ServerGameHandler(bus);
	GraphicsManager gr = new GraphicsManager(new DisplayMode(800, 800), true, true, bus);

	public static void main(String[] args) {
		Thread.currentThread().setName("Server");
		new ServerTest();
	}

	public ServerTest() {
		Thread graphicsThread = new Thread(gr);
		graphicsThread.setName("Server Graphics");
		graphicsThread.start();
		while (true) {
			g.update();
		}
	}
}
