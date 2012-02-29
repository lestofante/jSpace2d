package base.game.network.packets;

import java.nio.ByteBuffer;

public class PacketRecognizer {

	public static TCP_Packet getTCP(ByteBuffer in) throws Exception {

		switch (in.get()) {
		case -127:
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
		byte shipID = in.get();

		out = new LoginPacket(username, shipID);
		System.out.println("Recognized login packet");
		return out;
	}
}
