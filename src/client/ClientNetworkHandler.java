package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import base.game.network.NetworkHandler;
import base.game.network.packets.TCP_Packet;
import base.game.player.NetworkPlayer;
import base.worker.Worker;

public class ClientNetworkHandler extends NetworkHandler {

	public ClientNetworkHandler() throws IOException {
		super();
	}

	@Override
	public void read(ArrayList<Worker> w) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(HashMap<NetworkPlayer, ArrayList<TCP_Packet>> wOUT,
			ArrayList<Worker> wIN) {
		// TODO Auto-generated method stub
		
	}

}
