package base.game.network.packets.TCP.fromServer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;

import base.game.network.NetworkStream;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.utils.EntityInfo;
import base.game.network.packets.utils.PlayerInfo;
import base.game.player.Player;

public class SynchronizeMapPacket extends TCP_Packet {
	private static final int dimensionPlayer = 32;
	private static final int dimensionEntity = 4;
	public final Collection<PlayerInfo> playersInfo = new LinkedList<>();

	public SynchronizeMapPacket(Collection<Player> players, NetworkStream stream) {
		super(TCP_PacketType.SYNC_MAP, stream);
		extractInfo(players);
		createBuffer( calculateDimension() );
		setComplete(true); // we created it so it better be!
	}

	private void extractInfo(Collection<Player> players) {
		for (Player p : players) {
			playersInfo.add(new PlayerInfo(p.getEntities(), p.getPlayerName(), p.getPlayerID()));
		}
	}

	public SynchronizeMapPacket(ByteBuffer buffer, NetworkStream stream) {
		super(TCP_PacketType.SYNC_MAP, stream);
		this.buffer = buffer;
		setComplete(validateComplete());
	}
	
	private int calculateDimension(){
		int dimension = 2; // numero player
		for (PlayerInfo p : playersInfo) {
			dimension += dimensionPlayer;
			dimension += 2; // numero entity
			dimension += dimensionEntity * p.getEntitiesInfo().size();
		}
		return dimension;
	}

	@Override
	public void populateBuffer() {
		buffer.putChar((char) playersInfo.size());
		log.debug("Player number: {} {}", playersInfo.size(), (((char) playersInfo.size()) & 0xFF));

		for (PlayerInfo p : playersInfo) {
			buffer.putChar((char) p.getEntitiesInfo().size());
			log.debug("Entity number: {} {}", p.getEntitiesInfo().size(), (((char) p.getEntitiesInfo().size()) & 0xFF));

			buffer.putChar(p.getPlayerID()); // 2 byte
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

				for (EntityInfo e : p.getEntitiesInfo()) {
					buffer.putChar(e.entityID); // 2 byte
					buffer.putChar(e.blueprintID); // 2 byte
				}
			} catch (UnsupportedEncodingException e1) {
				log.error("Impossibile to cast to char: ", e1);
				e1.printStackTrace();
			}

		}
	}

	@Override
	protected boolean validateComplete() {
		log.debug("Packet SynchronizeMapPacket read, size: {}", buffer.remaining());
		log.debug("In buffer: {}", buffer);
		if (!mapPacket()) {
			log.debug("Input buffer underflow");
			return false;
		}
		return true;
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

		for (int i = 0; i < playerNumber; i++) {

			if (buffer.remaining() < 2) {
				// no number of entity present, add back how many player are
				// left and return underflow error
				buffer.position(buffer.position() - 1);
				buffer.put((byte) (playerNumber - i));
				log.debug("No entity number");
				return false;
			}

			int numberOfEntities = buffer.getChar() & 0xFF;

			log.debug("Entity number: " + numberOfEntities);

			// now we know how many entity for this player we have. we have just
			// to check if there are enough byte for this bunch read
			if (buffer.remaining() >= dimensionPlayer + numberOfEntities * dimensionEntity) {
				// we have enough byte to read ONE player and all of its entity!
				// read them all!!
				playersInfo.add(new PlayerInfo(buffer, numberOfEntities));

				log.debug("Added player and entity");
			} else {				
				log.debug("Not enough bytes: {} of {}", buffer.remaining(), dimensionPlayer + numberOfEntities * dimensionEntity);
				return false;
			}

		}
		// Everything went better than expected
		return true;
	}

	@Override
	public String toString() {
		String out = new String();
		out = out.concat("\n");
		for (PlayerInfo player : playersInfo) {
			out = out.concat("Player: " + player.getPlayerName());
			out = out.concat("\nplayer ID: " + String.valueOf((int) player.getPlayerID()) + "\n");
			for (EntityInfo eI : player.getEntitiesInfo()) {
				out = out.concat("\n\tEntity ID: " + eI.entityID);
				out = out.concat("\n\tentity type: " + eI.blueprintID);
			}
		}

		return out;
	}
}
