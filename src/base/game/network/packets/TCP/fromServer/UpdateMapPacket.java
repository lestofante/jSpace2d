package base.game.network.packets.TCP.fromServer;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;

import org.jbox2d.common.Vec2;

import base.game.entity.Entity;
import base.game.network.NetworkStream;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.utils.EntityInfo;

public class UpdateMapPacket extends TCP_Packet {

	private final Collection<EntityInfo> entitiesInfo = new LinkedList<>();

	public UpdateMapPacket(Collection<Entity> collection, NetworkStream stream) {
		super(TCP_PacketType.UPDATE_MAP, stream);
		extractInfo(collection);
		createBuffer(calculateDimension());
		setComplete(true); // we created it so it better be!
	}

	private int calculateDimension() {
		return 2 + entitiesInfo.size() * (2 + 4 + 4 + 4);
		// 2byte
		// number of
		// entities
		// +
		// for every entity:
		// 2 bytes for ID | 4
		// for
		// x position | 4
		// for y
		// position | 4 for
		// angle

	}

	public UpdateMapPacket(ByteBuffer buffer, NetworkStream stream) {
		super(TCP_PacketType.UPDATE_MAP, stream);
		this.buffer = buffer;
		setComplete(validateComplete());
	}

	private void extractInfo(Collection<Entity> entities) {
		for (Entity entity : entities) {
			entitiesInfo.add(new EntityInfo(entity.entityID, new Vec2(entity.infoBody.getTransform()[0], entity.infoBody.getTransform()[1]), entity.infoBody.getTransform()[2]));
		}
	}

	@Override
	protected boolean validateComplete() {
		if (!mapPacket()) {
			log.debug("Input buffer underflow");
			return false;
		}
		return true;
	}

	private boolean mapPacket() {
		if (buffer.remaining() < 2) {
			// no specific header present, return underflow error
			log.debug("No player number");
			return false;
		}
		log.debug("DECODING UPDATEMAP");
		int entityNumber = buffer.getChar() & 0xFF; // how many entity there
		// are
		log.debug("Entity number: " + entityNumber);
		log.debug("buffer: " + buffer.remaining());
		for (int i = 0; i < entityNumber; i++) {

			if (buffer.remaining() < 14) {
				log.debug("Unsufficient data to create entity");
				return false;
			}

			char entityID = buffer.getChar();
			log.debug("ID: " + (int) entityID);

			float x = buffer.getFloat();
			log.debug("x: " + x);
			float y = buffer.getFloat();
			log.debug("y: " + y);
			float angle = buffer.getFloat();
			log.debug("a: " + angle);

			entitiesInfo.add(new EntityInfo(entityID, new Vec2(x, y), angle));

		}
		log.debug("END: " + buffer.remaining());
		// Everything went better than expected
		return true;
	}

	@Override
	protected void populateBuffer() {
		log.debug("CREATING UPDATE MAP ");
		buffer.putChar((char) entitiesInfo.size());
		for (EntityInfo info : entitiesInfo) {
			buffer.putChar(info.entityID);
			log.debug("id: " + (int) info.entityID);
			buffer.putFloat(info.position.x);
			buffer.putFloat(info.position.y);
			buffer.putFloat(info.angle);
		}
	}

	public Collection<EntityInfo> getEntitiesInfo() {
		return entitiesInfo;
	}
}
