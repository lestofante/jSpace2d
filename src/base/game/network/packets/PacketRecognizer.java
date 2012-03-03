package base.game.network.packets;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.network.packets.TCP.LoginPacket;

public class PacketRecognizer {

	private static final Logger log = LoggerFactory.getLogger(base.game.network.packets.PacketRecognizer.class);

	public static TCP_Packet getTCP(ByteBuffer in) throws Exception {

		switch (in.get()) {
		case -128:
			return createLoginPacket(in);
		default:
			throw new Exception("Uknown packet type");
		}
	}

	private static LoginPacket createLoginPacket(ByteBuffer in) {
		LoginPacket out = null;

		char[] tmp = new char[30];
		for (int i = 0; i < 30; i++)
			tmp[i] = (char) in.get();
		String username = String.copyValueOf(tmp).trim();

		out = new LoginPacket(username);
		return out;
	}
}
