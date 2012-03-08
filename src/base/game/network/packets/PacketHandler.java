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
		boolean enoughtByteToRead = in.hasRemaining();
		while (enoughtByteToRead) {
			byte read = in.get();
			switch (read) {
			case 0:
				log.debug("Possible login packet read: {} {}", read, (read & 0xFF));
				LoginPacket pL = createLoginPacket(in);
				if (pL == null) {
					// underflow error, add back packet type, and terminate
					// reading cicle because we don't have enought data
					in.position(in.position() - 1);
					in.put(read);
					enoughtByteToRead = false;
				} else {
					out.add(pL);
				}
				break;
			case 1:
				log.debug("Possible player request packet read: {} {}", read, (read & 0xFF));
				PlayRequestPacket pPR = createPlayRequestPacket(in);
				if (pPR == null) {
					// underflow error, add back packet type, and terminate
					// reading cicle because we don't have enought data
					in.position(in.position() - 1);
					in.put(read);
					enoughtByteToRead = false;
				} else {
					out.add(pPR);
				}
				break;
			case 2:
				log.debug("Possible client action packet read: {} {}", read, (read & 0xFF));
				ClientActionPacket pCA = createClientActionPacket(in);
				if (pCA == null) {
					// underflow error, add back packet type, and terminate
					// reading cicle because we don't have enought data
					in.position(in.position() - 1);
					in.put(read);
					enoughtByteToRead = false;
				} else {
					out.add(pCA);
				}
				break;
			case 3:
				log.debug("Possible update map packet read: {} {}", read, (read & 0xFF));
				UpdateMapPacket pUM = createUpdateMapPacket(in);
				if (pUM == null) {
					// underflow error, add back packet type, and terminate
					// reading cicle because we don't have enought data
					in.position(in.position() - 1);
					in.put(read);
					enoughtByteToRead = false;
				} else {
					out.add(pUM);
				}
				break;
			default:
				log.error("readed: " + read);
				// put it back?!
				in.position(in.position() - 1);
				in.put(read);
			}

			if (!in.hasRemaining()) {
				// if the buffer is over without throwing any error (over any
				// expectation)
				enoughtByteToRead = false;
				throw new Exception("Uknown packet type");
			}
		}
		return out;
	}

	private static UpdateMapPacket createUpdateMapPacket(ByteBuffer in) {
		UpdateMapPacket out = new UpdateMapPacket(in);
		if (out.isValid()) {
			return out;
		} else {
			return null;
		}
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
}
