package server.network;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.worker.ServerWorker;
import base.game.network.packets.TCP_Packet;

public class ServerNetworkHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final LoginHandler loginHandler;
	private final ClientHandler clientHandler;

	public ServerNetworkHandler() throws IOException {
		super();
		loginHandler = new LoginHandler(9999);
		clientHandler = new ClientHandler(getNetworkMTU());
	}

	private int getNetworkMTU() {
		return 1500;
	}

	public void read(List<ServerWorker> wIN) {
		try {
			loginHandler.acceptNewConnection();
			loginHandler.checkForLoginRequests(wIN);
			clientHandler.read(wIN);
		} catch (Exception e) {
			log.error("Error reading from network", e);
		}
	}

	public SelectionKey addConnectedClient(ServerNetworkStream stream) {
		try {
			return clientHandler.addConnectedClient(stream);
		} catch (ClosedChannelException e) {
			log.error("Error adding client", e);
		}
		return null;
	}

	public void write(List<TCP_Packet> wOUT, List<ServerWorker> wIN) {
		clientHandler.write(wOUT, wIN);
	}

}
