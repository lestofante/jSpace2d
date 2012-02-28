package base.game.network.packets;

import java.nio.ByteBuffer;

public abstract class TCP_Packet {
	
	public enum PacketType {
		LOGIN
	}

	public final PacketType packetType;
	
	public TCP_Packet(PacketType packetType){
		this.packetType = packetType;
	}
	
	public abstract ByteBuffer getDataBuffer();
	
}
