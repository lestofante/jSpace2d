package base.game.network.packets.TCP.fromServer;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import base.game.entity.Entity;
import base.game.network.NetworkStream;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.utils.EntityInfo;

public class UpdateMapPacket extends TCP_Packet {

	private final Collection<EntityInfo> entitiesInfo = new LinkedList<>();

	public UpdateMapPacket(List<Entity> entities, NetworkStream stream) {
		super(TCP_PacketType.UPDATE_MAP, stream);
		extractInfo(entities);
	}

	public UpdateMapPacket(ByteBuffer buffer, NetworkStream stream) {
		super(TCP_PacketType.UPDATE_MAP, stream);
		this.buffer = buffer;
		setComplete(validateComplete());
	}

	private void extractInfo(List<Entity> entities) {
		// for(Entity entity: entities)

	}

	@Override
	protected boolean validateComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void populateBuffer() {
		// TODO Auto-generated method stub
		
	}

}
