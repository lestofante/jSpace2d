package base.game.network.packets.TCP;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

import base.game.entity.Entity;
import base.game.network.packets.TCP_Packet;
import base.game.player.Player;

public class UpdateMapPacket extends TCP_Packet {
	private static final int dimensionPlayer = 32;
	private static final int dimensionEntity = 32;
	Collection<Entity> entities;
	Collection<Player> players;

	public UpdateMapPacket(Collection<Entity> entitys, Collection<Player> players) {
		super(TCP_PacketType.UPDATE_MAP);
		this.entities = entitys;
		this.players = players;
		createBuffer();
	}

	public UpdateMapPacket(ByteBuffer buffer) {
		super(TCP_PacketType.UPDATE_MAP);
		this.buffer = buffer;
		entities = new ArrayList<>();
		players = new ArrayList<>();
		recognizePacket();
	}

	@Override
	public void createBuffer() {
		buffer = ByteBuffer.allocate(dimensionPlayer * players.size() + dimensionEntity * entities.size() + 1);
		buffer.clear();
		buffer.put((byte) -125);

		for (Player p : players) {
			buffer.putChar(p.playerID);

			byte array[];
			try {
				array = p.getPlayerName().getBytes("ASCII");
				int i = 0;
				for (; i < array.length; i++)
					buffer.put(array[i]);
				for (; i < 30; i++) {
					buffer.put((byte) 32);
				}
			} catch (UnsupportedEncodingException e1) {
				log.error("Impossibile castare a char: ", e1);
				e1.printStackTrace();
			}

		}

		for (Entity e : entities) {
			buffer.putChar(e.entityID);
			buffer.putChar(e.owner.playerID);
			buffer.putChar(e.blueprintID);
		}

		buffer.flip();

	}

	@Override
	protected void recognizePacket() {
		while (buffer.hasRemaining()) {
			char next = buffer.getChar();

			if (next % 2 == 0)
				players.add(recognizePlayer(next));
			else
				entities.add(recognizeEntity(next));
		}
	}

	private Player recognizePlayer(char next) {
		return new Player(next, getPlayerName());
	}

	private String getPlayerName() {
		char[] tmp = new char[30];
		for (int i = 0; i < 30; i++) {
			tmp[i] = (char) buffer.get();
		}
		return String.copyValueOf(tmp).trim();
	}

	private Entity recognizeEntity(char next) {
		char playerID = buffer.getChar();
		char bluePrintID = buffer.getChar();
		return new Entity(next, bluePrintID, null);
	}

	@Override
	public String toString() {
		String out = new String();
		out = out.concat("\n");
		for (Player player : players) {
			out = out.concat("Player: " + player.getPlayerName());
			out = out.concat("\nplayer ID: " + String.valueOf((int) player.playerID) + "\n");
		}
		out = out.concat("\n");
		for (Entity entity : entities) {
			out = out.concat("Entity id: " + String.valueOf((int) entity.entityID));
			out = out.concat("\nEntity owner's id: cannot calculate");
			out = out.concat("\nEntity blueprint's id: " + String.valueOf((int) entity.blueprintID) + "\n");
		}

		return out;
	}
}
