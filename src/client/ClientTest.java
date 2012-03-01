package client;

import org.lwjgl.opengl.DisplayMode;

import base.common.AsyncActionBus;
import base.graphics.GraphicsManager;

public class ClientTest {

	/**
	 * @param args
	 */

	AsyncActionBus bus = new AsyncActionBus();
	ClientGameHandler g = new ClientGameHandler(bus, "12343a1", "127.0.0.1");
	GraphicsManager gr = new GraphicsManager(new DisplayMode(800, 800), true, true, bus);

	public static void main(String[] args) {
		new ClientTest();
	}

	public ClientTest() {
		Thread.currentThread().setName("Client");
	}

}
