package base.game.network.packets.TCP.toServer;

import java.nio.ByteBuffer;

import base.game.network.NetworkStream;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP_Packet.TCP_PacketType;

public class RequestEntity extends TCP_Packet{

	private int dimension = 4;
	int typeID;
	
	public RequestEntity(NetworkStream stream, int typeID) {
		super(TCP_PacketType.REQUEST, stream);
		this.typeID = typeID;
		createBuffer(dimension);
		setComplete(true); // we created it so it better be!
	}
	
	public RequestEntity(ByteBuffer in, NetworkStream stream) {
		super(TCP_PacketType.LOGIN, stream);
		setComplete(validateComplete());
	}

	@Override
	protected boolean validateComplete() {
		if (buffer.remaining() < dimension )
			return false;
		typeID = buffer.getInt(); 
		return true;
	}

	@Override
	protected void populateBuffer() {
		buffer.putInt(typeID);
	}

}
