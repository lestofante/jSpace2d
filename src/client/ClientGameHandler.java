package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import server.entity.ServerEntityHandler;
import server.net.ServerNetworkHandler;
import server.player.ServerPlayerHandler;

import base.common.AsyncActionBus;
import base.game.GameHandler;
import base.game.network.packets.PacketHandler;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP_Packet.TCP_PacketType;
import base.game.network.packets.TCP.LoginPacket;
import base.worker.Worker;

public class ClientGameHandler extends GameHandler {

	public ClientGameHandler(AsyncActionBus bus, String clientName, String serverAddress, int serverPort) {
		this.entityHandler = new ServerEntityHandler(bus, step);

		try {
			this.playerHandler = new ClientPlayerHandler();
			this.networkHandler = new ClientNetworkHandler();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public void update() {
		
		try {
			networkHandler.read(wIN);
			//che sistema di input?!?!?!
			inputHandler.read(wIN);
			
			for (Worker wTmp : wIN) {
				wTmp.execute(this);
			}

			wIN.clear();			
			
			//playerHandler.update(wIN);
			//entityHandler.update(wIN);

			for (Worker wTmp : wIN) {
				wTmp.execute(this);
			}
			wIN.clear();

			networkHandler.write(wOUT, wIN);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
