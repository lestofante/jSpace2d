package client;

import org.lwjgl.opengl.DisplayMode;

import base.common.AsyncActionBus;
import base.game.entity.graphics.GraphicsManager;

public class ClientTest {

	/**
	 * @param args
	 */

	static AsyncActionBus bus = new AsyncActionBus();
	static ClientGameHandler g = new ClientGameHandler(bus);
	static GraphicsManager gr = new GraphicsManager(new DisplayMode(800, 800), true, true, bus);

	public static void main(String[] args) {
		/*
		 * Thread graphicsThread = new Thread(gr); graphicsThread.start();
		 * while(true){ g.update(); }
		 */

	}

}
