package server.net;

import java.io.IOException;
import java.util.ArrayList;

import base.game.network.NetworkHandler;
import base.worker.Worker;

public class ServerNetworkHandler extends NetworkHandler {

	public ServerNetworkHandler() throws IOException {
		super(new ServerSelectorHandler(getNetworkMTU()));
	}

	@Override
	public void read(ArrayList<Worker> w) {
		try {
			w.addAll(super.update());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void write(ArrayList<Worker> wOUT) {
		// TODO Auto-generated method stub

	}

}
