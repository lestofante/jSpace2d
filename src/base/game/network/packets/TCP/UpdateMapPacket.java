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
	private static final int dimensionEntity = 4;
	Collection<Entity> entities;
	Collection<Player> players;

	public UpdateMapPacket(Collection<Entity> entitys, Collection<Player> players) {
		super(TCP_PacketType.UPDATE_MAP);
		this.entities = entitys;
		this.players = players;
		createBuffer();
		setComplete(true); // we created it so it better be!
	}

	public UpdateMapPacket(ByteBuffer buffer) {
		super(TCP_PacketType.UPDATE_MAP);
		this.buffer = buffer;
		entities = new ArrayList<>();
		players = new ArrayList<>();
		setComplete(recognizePacket());
	}

	@Override
	public void createBuffer() {
		log.debug("Writing map update");
		int dimension = 1; // tipo azione

		dimension += 2; // numero player
		for (Player p : players) {
			dimension += dimensionPlayer;
			dimension += 2; // numero entity
			dimension += dimensionEntity * p.getEntities().size();
		}

		buffer = ByteBuffer.allocate(dimension);
		buffer.clear();
		buffer.put((byte) 3);

		buffer.putChar((char) players.size());
		log.debug("Player number: {} {}", players.size(), (((char) players.size()) & 0xFF));

		for (Player p : players) {
			buffer.putChar((char) p.getEntities().size());
			log.debug("Entity number: {} {}", p.getEntities().size(), (((char) p.getEntities().size()) & 0xFF));

			buffer.putChar(p.playerID); // 2 byte
			byte array[];
			try {
				array = p.getPlayerName().getBytes("ASCII");
				int i = 0;
				for (; i < array.length; i++)
					// 30 byte
					buffer.put(array[i]);
				for (; i < 30; i++) {
					buffer.put((byte) 32); // add "blank"
				}

				for (Entity e : p.getEntities()) {
					buffer.putChar(e.entityID); // 2 byte
					buffer.putChar(e.blueprintID); // 2 byte
				}
			} catch (UnsupportedEncodingException e1) {
				log.error("Impossibile to cast to char: ", e1);
				e1.printStackTrace();
			}

		}

		buffer.flip();

		log.debug("out buffer: " + buffer);

	}

	@Override
	protected boolean recognizePacket() {
		/*
		
		*/
		log.debug("Packet UpdateMapPacket read, size: {}", buffer.remaining());
		log.debug("In buffer: {}", buffer);
		if (!mapPacket()) {
			log.debug("Input buffer underflow");
		}
		return false;
	}

	protected boolean mapPacket() {
		if (buffer.remaining() < 2) {
			// no specific header present, return underflow error
			log.debug("No player number");
			return false;
		}

		int playerNumber = buffer.getChar() & 0xFF; // how many player there
													// are?

		log.debug("Player number: {}", playerNumber);

		Player lastPlayer = null;
		for (int i = 0; i < playerNumber; i++) {

			if (buffer.remaining() < 2) {
				// no number of entity present, add back how many player are
				// left and return underflow error
				buffer.position(buffer.position() - 1);
				buffer.put((byte) (playerNumber - i));
				log.debug("No entity number");
				return false;
			}
			int entityNumber = buffer.getChar() & 0xFF;

			log.debug("Entity number: " + entityNumber);

			// now we know how many entity for this player we have. we have just
			// to check if there are enough byte for this bunch read
			if (buffer.remaining() >= dimensionPlayer + entityNumber * dimensionEntity) {
				// we have enough byte to read ONE player and all of its entity!
				// read them all!!
				lastPlayer = recognizePlayer(buffer);
				players.add(lastPlayer);

				for (int a = 0; a < entityNumber; a++) {
					entities.add(recognizeEntity(buffer, lastPlayer));
				}
				log.debug("Added player and entity");
			} else {
				// no player+entitys data present, add back how many player are
				// left, the number of entity for this player and return
				// underflow error
				buffer.position(buffer.position() - 2);
				buffer.put((byte) (playerNumber - i));
				buffer.put((byte) entityNumber);
				log.debug("Not enough bytes: {} of {}", buffer.remaining(), dimensionPlayer + entityNumber * dimensionEntity);
				return false;
			}

		}
		// Everything went better than expected
		return true;
	}

	private Player recognizePlayer(ByteBuffer buffer) {
		return new Player(buffer.getChar(), getPlayerName());
	}

	private String getPlayerName() {
		char[] tmp = new char[30];
		for (int i = 0; i < 30; i++) {
			tmp[i] = (char) buffer.get();
		}
		return String.copyValueOf(tmp).trim();
	}

	private Entity recognizeEntity(ByteBuffer buffer, Player possessor) {
		char bluePrintID = buffer.getChar();

		return new Entity(buffer.getChar(), bluePrintID, possessor);
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
			out = out.concat("Entity ID: " + String.valueOf((int) entity.entityID));
			out = out.concat("\nEntity owner's id: " + String.valueOf((int) entity.owner.playerID));
			out = out.concat("\nEntity blueprint's id: " + String.valueOf((int) entity.blueprintID) + "\n");
		}

		return out;
	}
}
