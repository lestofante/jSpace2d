package client;

import java.io.IOException;
import java.util.ArrayList;

import base.game.network.NetworkHandler;
import base.worker.Worker;

public class ClientNetworkHandler extends NetworkHandler {

	public ClientNetworkHandler() throws IOException {
		super(new ClientSelectorHandler());
		// TODO Auto-generated constructor stub
	}

	@Override
	public void read(ArrayList<Worker> w) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(ArrayList<Worker> wOUT) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void update(ArrayList<Worker> w) throws IOException {
		// TODO Auto-generated method stub

	}

}
