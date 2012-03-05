package base.game.network.packets;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.network.packets.TCP.LoginPacket;
import base.game.network.packets.TCP.UpdateMapPacket;

public class PacketRecognizer {

	private static final Logger log = LoggerFactory.getLogger(base.game.network.packets.PacketRecognizer.class);

	public static TCP_Packet getTCP(ByteBuffer in) throws Exception {

		switch (in.get()) {
		case -128:
			return createLoginPacket(in);
		case -127:
			return createPlayRequestPacket(in);
		case -126:
			return createClientActionPacket(in);
		case -125:
			return createUpdateMapPacket(in);
		default:
			throw new Exception("Uknown packet type");
		}
	}

	private static TCP_Packet createUpdateMapPacket(ByteBuffer in) {
		return new UpdateMapPacket(in);
	}

	private static TCP_Packet createClientActionPacket(ByteBuffer in) {
		// TODO Auto-generated method stub
		return null;
	}

	private static TCP_Packet createPlayRequestPacket(ByteBuffer in) {
		// TODO Auto-generated method stub
		return null;
	}

	private static LoginPacket createLoginPacket(ByteBuffer in) {
		return new LoginPacket(in);
	}
}
