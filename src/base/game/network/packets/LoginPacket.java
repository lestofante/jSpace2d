package base.game.network.packets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class LoginPacket extends TCP_Packet {

	private final String username;

	public String getUsername() {
		return username;
	}

	public byte getShipID() {
		return shipID;
	}

	private final byte shipID;

	public LoginPacket(String userName, byte shipID) {
		super(PacketType.LOGIN);
		if (userName.length() > 30)
			userName = userName.substring(0, 30);
		this.username = userName;
		this.shipID = shipID;
	}

	@Override
	public ByteBuffer getDataBuffer() {
		ByteBuffer out = ByteBuffer.allocate(32);
		out.clear();
		out.put((byte) -127);

		int i = 0;
		try {
			for (; i < username.getBytes("ASCII").length; i++)
				out.put(username.getBytes("ASCII")[i]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		for (; i < 30; i++)
			out.put((byte) 32);
		out.put(shipID);
		out.flip();
		return out;
	}

}
