/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base.game.network.actions;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * 
 * @author mauro
 */
public abstract class NetworkAction {
	
	public byte actionID;
	public InetSocketAddress address;
	public int actionSize;
	
	protected NetworkAction(InetSocketAddress address, byte actionID, int actionSize){
		this.actionSize = actionSize;
		this.address = address;
		this.actionID = actionID;
	}
	
	public void writeToByteBuffer(ByteBuffer toWrite){
		toWrite.put(actionID);
	}
	
	
}
