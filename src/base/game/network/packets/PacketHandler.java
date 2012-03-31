package base.game.network.packets;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.network.NetworkStream;
import base.game.network.packets.TCP_Packet.TCP_PacketType;
import base.game.network.packets.TCP.fromServer.SynchronizeMapPacket;
import base.game.network.packets.TCP.fromServer.UpdateMapPacket;
import base.game.network.packets.TCP.toServer.ClientActionPacket;
import base.game.network.packets.TCP.toServer.LoginPacket;
import base.game.network.packets.TCP.toServer.PingResponsePacket;
import base.game.network.packets.TCP.toServer.RequestEntityPacket;

public class PacketHandler {

	private static final Logger log = LoggerFactory.getLogger(base.game.network.packets.PacketHandler.class);

	public static ArrayList<TCP_Packet> getTCP(ByteBuffer in, NetworkStream stream) throws Exception {
		ArrayList<TCP_Packet> out = new ArrayList<>();
		TCP_Packet pOut = null;
		boolean enoughtByteToRead = in.hasRemaining();

		TCP_PacketType[] TCPvalues = TCP_PacketType.values();
		int lastBufferPosition = in.position();
		while (enoughtByteToRead) {
			byte read = in.get();
			if (read > TCPvalues.length) {
				throw new Exception("Corrupted input buffer!");
			}
			switch (TCPvalues[read]) {
			case LOGIN:
				pOut = createLoginPacket(in, stream);
				break;
			case CLIENT_ACTION:
				pOut = createClientActionPacket(in, stream);
				break;
			case SYNC_MAP:
				pOut = createSynchronizeMapPacket(in, stream);
				break;
			case UPDATE_MAP:
				pOut = createUpdateMapPacket(in, stream);
				break;
			case REQUEST:
				pOut = createRequesPacket(in, stream);
				break;
			case PING_RESPONSE:
				pOut = createPingResponsePacket(in, stream);
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
				lastBufferPosition = in.position();
				if (!in.hasRemaining()) {
					enoughtByteToRead = false;
					in.clear();
				}
			} else {
				// underflow error, add back packet type, and terminate
				// reading cycle because we don't have enough data
				in.position(lastBufferPosition);

				// eliminate not needed data
				in.compact();
				// set position to the beginning of the buffer (compact does not
				// do it)
				in.rewind();

				enoughtByteToRead = false;
				pOut = null; // useless, but it makes me feel safe
			}
		}
		return out;
	}

	private static TCP_Packet createPingResponsePacket(ByteBuffer in, NetworkStream stream) {
		return new PingResponsePacket(in, stream);
	}

	private static TCP_Packet createRequesPacket(ByteBuffer in, NetworkStream stream) {
		return new RequestEntityPacket(in, stream);
	}

	private static TCP_Packet createUpdateMapPacket(ByteBuffer in, NetworkStream stream) {
		return new UpdateMapPacket(in, stream);
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

}
