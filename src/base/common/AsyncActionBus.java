package base.common;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import base.game.actions.GameAction;
import base.game.entity.graphics.actions.GraphicAction;

/**
 * 
 * @author mauro
 */
public class AsyncActionBus {

	public ReentrantReadWriteLock sharedLock;
	
	public AsyncActionBus(){
		sharedLock = new ReentrantReadWriteLock(false);
	}
	
	/*
	 * GRAPHICS
	 */
	private ConcurrentLinkedQueue<GraphicAction> graphicActions = new ConcurrentLinkedQueue<>();

	/*
	 * NETWORK
	 */
	private LinkedBlockingQueue<ByteBuffer> rawPacketsOut = new LinkedBlockingQueue<>();
	private ConcurrentLinkedQueue<ByteBuffer> rawPacketsIn = new ConcurrentLinkedQueue<>();
	
	/*
	 * GAME
	 */
	private ConcurrentLinkedQueue<GameAction> gameActions = new ConcurrentLinkedQueue<>();
		
	public void addGameAction(GameAction gameAction){
		gameActions.offer(gameAction);
	}
	
	public ArrayList<GameAction> getGameActions(){
		ArrayList<GameAction> returnActions = new ArrayList<>();
		GameAction temp = gameActions.poll();
		while(temp!=null){
			returnActions.add(temp);
			temp = gameActions.poll();
		}
		return returnActions;
	}
	
	public void addGraphicsAction(GraphicAction a) {
		graphicActions.offer(a);
	}

	public ArrayList<GraphicAction> getGraphicActions() {
		ArrayList<GraphicAction> returnActions = new ArrayList<>();
		GraphicAction temp = graphicActions.poll();
		while(temp!=null){
			returnActions.add(temp);
			temp = graphicActions.poll();
		}
		return returnActions;
	}

	public void addRawNetworkOut(ArrayList<ByteBuffer> rawActions) {
		rawPacketsOut.addAll(rawActions);
	}
	
	public ArrayList<ByteBuffer> getRawNetworkIn(){
		ArrayList<ByteBuffer> out = new ArrayList<>();
		ByteBuffer temp = rawPacketsIn.poll();
		while(temp!=null){
			out.add(temp);
			temp = rawPacketsIn.poll();
		}
		return out;
	}
}
