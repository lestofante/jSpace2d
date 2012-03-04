package base.game.network.packets.TCP;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.util.Collection;

import base.game.entity.Entity;
import base.game.network.packets.TCP_Packet;
import base.game.player.Player;

public class UpdateMapPacket extends TCP_Packet {
	private static final int dimensionPlayer = 32;
	private static final int dimensionEntity = 32;
	Collection<Entity> entity;
	Collection<Player> player;
	
	public UpdateMapPacket(Collection<Entity> entitys, Collection<Player> players) {
		super(TCP_PacketType.UPDATE_MAP);
		this.entity = entitys;
		this.player = players;
		createBuffer();
	}

	@Override
	public void createBuffer() {
		buffer = ByteBuffer.allocate(dimensionPlayer*player.size() + dimensionEntity * entity.size());
		for (Player p : player){
			buffer.putChar( p.playerID );
			
			byte array[];
			try {
				array = p.getPlayerName().getBytes("ASCII");
				int i=0;
				for (; i < array.length; i++)
					buffer.put(array[i]);
				for (; i < 30; i++){
					buffer.put((byte) 32);
				}
			} catch (UnsupportedEncodingException e1) {
				log.error("Impossibile castare a char: ", e1);
				e1.printStackTrace();
			}
			
		}
		
		for (Entity e : entity){
			buffer.putChar( e.entityID );
			buffer.putChar( e.owner.playerID );
			buffer.putChar( e.blueprintID );
		}
		
	}

}
