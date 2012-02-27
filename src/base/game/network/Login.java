package base.game.network;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import base.game.GameHandler;
import base.game.player.worker.CreateNetworkPlayer;
import base.worker.NetworkWorker;

public class Login extends NetworkWorker{

	Byte idAstronave;
	CreateNetworkPlayer player = new CreateNetworkPlayer();
	
	public void write(ByteBuffer buf){
		buf.put( (byte)NetworkWorker.PacketType.Login.ordinal() );
		player.write(buf);
		buf.put(idAstronave);
	}

	@Override
	public void update(GameHandler g) {
		player.update(g);
	}

	@Override
	public boolean read(ByteBuffer buf) {
		try{
			idAstronave=buf.get();
			return true;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	public void setKey(SelectionKey key) {
		player.setKey(key);
	}

}
