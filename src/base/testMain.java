package base;

import org.lwjgl.opengl.DisplayMode;

import base.common.AsyncActionBus;
import base.game.GameHandler;
import base.game.entity.graphics.GraphicsManager;

public class testMain {
	static AsyncActionBus bus = new AsyncActionBus();
	static GameHandler g = new GameHandler(bus);
	static GraphicsManager gr = new GraphicsManager(new DisplayMode(1920,1080), true, true, bus);
	
	public static void main(String args[]){
		Thread graphicsThread = new Thread(gr);
		graphicsThread.start();
		while(true){
			g.update();
		}
	}
	
	
}
