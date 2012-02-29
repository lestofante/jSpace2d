package base.game.network.packets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class LoginPacket extends TCP_Packet {

	private final String userName;
	private final byte shipID;

	public LoginPacket(String userName, byte shipID) {
		super(PacketType.LOGIN);
		if (userName.length() > 30)
			userName = userName.substring(0, 30);
		this.userName = userName;
		this.shipID = shipID;
	}

	@Override
	public ByteBuffer getDataBuffer() {
		ByteBuffer out = ByteBuffer.allocate(32);
		out.clear();
		out.put((byte) -127);

		int i = 0;
		try {
			for (; i < userName.getBytes("ASCII").length; i++)
				out.put(userName.getBytes("ASCII")[i]);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (; i < 30; i++)
			out.put((byte) -127);
		out.put(shipID);
		out.flip();
		return out;
	}

}
