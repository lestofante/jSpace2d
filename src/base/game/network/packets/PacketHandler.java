package base.game.network.packets;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.network.packets.TCP.ClientActionPacket;
import base.game.network.packets.TCP.LoginPacket;
import base.game.network.packets.TCP.PlayRequestPacket;
import base.game.network.packets.TCP.UpdateMapPacket;

public class PacketHandler {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(base.game.network.packets.PacketHandler.class);

	public static ArrayList<TCP_Packet> getTCP(ByteBuffer in) throws Exception {
		ArrayList<TCP_Packet> out = new ArrayList<>();
		TCP_Packet pOut = null;
		boolean enoughtByteToRead = in.hasRemaining();

		while (enoughtByteToRead) {
			byte read = in.get();
			switch (read) {
			case 0:
				log.debug("Possible login packet read: {} {}", read, (read & 0xFF));
				pOut = createLoginPacket(in);
				break;
			case 1:
				log.debug("Possible player request packet read: {} {}", read, (read & 0xFF));
				pOut = createPlayRequestPacket(in);
				break;
			case 2:
				log.debug("Possible client action packet read: {} {}", read, (read & 0xFF));
				pOut = createClientActionPacket(in);
				break;
			case 3:
				log.debug("Possible update map packet read: {} {}", read, (read & 0xFF));
				pOut = createUpdateMapPacket(in);
				break;
			default:
				/*
				 * FATAL: you screwed up big time!
				 * or in other words, we don't know what kind of packet we got!
				 */
				log.error("read unknown packet type: {}", read);

				/*
				 * You are going down!
				 */
				throw new Exception("Uknown packet type");
			}

			/*
			 * if the packet is complete add it to the the outgoing arraylist
			 * else, restore buffer and return the arraylist with the complete packets
			 */
			if (pOut.isComplete()) {
				out.add(pOut);
			} else {
				// underflow error, add back packet type, and terminate
				// reading cicle because we don't have enought data
				in.position(in.position() - 1);
				in.put(read);
				enoughtByteToRead = false;
				pOut = null; // useless, but it makes me feel safe
			}
		}
		return out;
	}

	private static UpdateMapPacket createUpdateMapPacket(ByteBuffer in) {
		return new UpdateMapPacket(in);
	}

	private static ClientActionPacket createClientActionPacket(ByteBuffer in) {
		return new ClientActionPacket(in);
	}

	private static PlayRequestPacket createPlayRequestPacket(ByteBuffer in) {
		return new PlayRequestPacket(in);
	}

	private static LoginPacket createLoginPacket(ByteBuffer in) {
		return new LoginPacket(in);
	}

	/**
	 * This method checks whether we had a login attempt. If the login packet is
	 * successfully created, we return the packet. (also the ByteBuffer's
	 * position will be updated, ready to be read by the ClientHandler) else we
	 * return null
	 * 
	 * @param dst
	 *            the ByteBuffer to check
	 * @return the received LoginPacket or null if none
	 */

	public static LoginPacket getLogin(ByteBuffer dst) {
		// TODO Auto-generated method stub
		return null;
	}
}
