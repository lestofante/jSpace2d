package base.game.network.packets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class LoginPacket extends TCP_Packet {

	private final String userName;
	private final byte shipID;

	public LoginPacket(String userName, byte shipID) {
		super(PacketType.LOGIN);
		this.userName = userName.substring(0, 30);
		this.shipID = shipID;
	}

	@Override
	public ByteBuffer getDataBuffer() {
		ByteBuffer out = ByteBuffer.allocate(32);
		out.clear();
		out.put((byte) -127);

		for (int i = 0; i < 30; i++)
			try {
				out.put(userName.getBytes("ASCII")[i]);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		out.put(shipID);
		out.flip();
		return out;
	}

}
