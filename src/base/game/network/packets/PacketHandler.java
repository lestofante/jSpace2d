package base.game.network.packets;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.network.NetworkStream;
import base.game.network.packets.TCP.fromServer.SynchronizeMapPacket;
import base.game.network.packets.TCP.toServer.ClientActionPacket;
import base.game.network.packets.TCP.toServer.LoginPacket;

public class PacketHandler {

	private static final Logger log = LoggerFactory.getLogger(base.game.network.packets.PacketHandler.class);

	public static ArrayList<TCP_Packet> getTCP(ByteBuffer in, NetworkStream stream) throws Exception {
		ArrayList<TCP_Packet> out = new ArrayList<>();
		TCP_Packet pOut = null;
		boolean enoughtByteToRead = in.hasRemaining();

		while (enoughtByteToRead) {
			byte read = in.get();
			switch (read) {
			case 0:
				log.debug("Possible login packet read: {} {}", read, (read & 0xFF));
				pOut = createLoginPacket(in, stream);
				break;
			case 1:
				log.debug("Possible client action packet read: {} {}", read, (read & 0xFF));
				pOut = createClientActionPacket(in, stream);
				break;
			case 2:
				log.debug("Possible update map packet read: {} {}", read, (read & 0xFF));
				pOut = createSynchronizeMapPacket(in, stream);
				break;
			default:
				/*
				 * FATAL: you screwed up big time!
				 * or in other words, we don't know what kind of packet we got!
				 * (and what to do with corrupted buffer)
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
				if (!in.hasRemaining()) {
					enoughtByteToRead = false;
					in.clear();
				}
			} else {
				// underflow error, add back packet type, and terminate
				// reading cycle because we don't have enough data

				// shift remaining bytes to the beginning of the buffer (putting
				// the read byte back in)
				in.compact();

				enoughtByteToRead = false;
				pOut = null; // useless, but it makes me feel safe
			}
		}
		return out;
	}

	private static SynchronizeMapPacket createSynchronizeMapPacket(ByteBuffer in, NetworkStream stream) {
		return new SynchronizeMapPacket(in, stream);
	}

	private static ClientActionPacket createClientActionPacket(ByteBuffer in, NetworkStream stream) {
		return new ClientActionPacket(in, stream);
	}

	private static LoginPacket createLoginPacket(ByteBuffer in, NetworkStream stream) {
		return new LoginPacket(in, stream);
	}

	/**
	 * This method checks whether we had a login attempt. If the login packet is
	 * successfully created, we return the packet. Else we return null
	 * 
	 * @param in
	 *            the ByteBuffer to check
	 * @return the received LoginPacket or null if none
	 */

	public static LoginPacket getLogin(ByteBuffer in, NetworkStream stream) {
		int read = in.get() & 0xFF;
		log.debug("Should be login type (0). Read: {}", read);

		if (read == 0) {
			log.debug("OK");
			LoginPacket pOut = createLoginPacket(in, stream);

			if (pOut.isComplete()) {
				return pOut;
			} else {
				log.debug("Login packet not complete");
			}
		} else {
			log.debug("NOPE");
			log.error("Uknown packet type");
		}
		return null;
	}
}
