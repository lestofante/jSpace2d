package base.game.player.worker;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import base.game.GameHandler;
import base.worker.NetworkWorker;

public class CreateNetworkPlayer extends NetworkWorker {
	private static final int usernameSize = 20;
	private static final int passwordSize = 20;

	String username;
	String password;
	SelectionKey key;

	@Override
	public boolean read(ByteBuffer buf) {
		try {
			for (int i = 0; i < usernameSize; i++)
				username += buf.getChar();
			for (int i = 0; i < passwordSize; i++)
				password += buf.getChar();
			username = username.trim();
			password = password.trim();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void setKey(SelectionKey key) {
		this.key = key;
	}

	@Override
	public void update(GameHandler g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(ByteBuffer buf) {
		int i = 0;
		for (; i < username.length(); i++)
			buf.putChar(username.charAt(i));
		for (; i < usernameSize; i++) {
			buf.putChar(' ');
		}

		i = 0;
		for (; i < password.length(); i++)
			buf.putChar(username.charAt(i));
		for (; i < passwordSize; i++) {
			buf.putChar(' ');
		}
	}

}
