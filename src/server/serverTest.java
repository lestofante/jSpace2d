package server;

import org.lwjgl.opengl.DisplayMode;

import base.common.AsyncActionBus;
import base.game.entity.graphics.GraphicsManager;

public class serverTest {

	/**
	 * @param args
	 */
	
	static AsyncActionBus bus = new AsyncActionBus();
	static ServerGameHandler g = new ServerGameHandler(bus);
	static GraphicsManager gr = new GraphicsManager(new DisplayMode(1920,1080), true, true, bus);
	
	public static void main(String[] args) {
		Thread graphicsThread = new Thread(gr);
		graphicsThread.start();
		while(true){
			g.update();
		}
	}

}
