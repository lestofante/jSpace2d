package server.net.workers;

import java.nio.channels.SocketChannel;

import server.ServerGameHandler;
import base.game.network.packets.LoginPacket;
import base.game.player.worker.CreateNetworkPlayer;
import base.worker.ServerWorker;

public class Login extends ServerWorker {

	byte shipID;
	String username;
	CreateNetworkPlayer createPlayerWorker;
	AddConnectedClient addConnectedClientWorker;
	SocketChannel channel;

	public String getUsername() {
		return username;
	}

	public SocketChannel getChannel() {
		return channel;
	}

	public Login(LoginPacket packet) {
		this.shipID = packet.getShipID();
		this.username = packet.getUsername();
	}

	public void setChannel(SocketChannel channel) {
		this.channel = channel;
	}

	@Override
	public int execute(ServerGameHandler g) {
		createPlayerWorker = new CreateNetworkPlayer(username, shipID, channel);
		if (createPlayerWorker.execute(g) == 0) {
			addConnectedClientWorker = new AddConnectedClient(g.playerHandler.getPlayer(username), channel);
			addConnectedClientWorker.execute(g);
		} else {
			return -1;
		}
		return 0;

		/*
		 * ci sei? sì allora il problema non si gra quì, a the che eccezzione
		 * dice?
		 */
		/*
		 * ci sei? si aspe, dove siamo finiti?! :) :) siamo nel worker login.
		 * una volta ricevuto il pacchetto di login da un client, viene creato
		 * questo worker quando viene eseguito il worker ne crea un altro per
		 * creare il giocatore e a questo punto come dicevi tu, registriamo il
		 * channel nel selector, ok? perfetto creiamo un altro worker? ah,
		 * piccolo particolare bisogna ancora sistemare il connection manager
		 * guarda sì un worker apposito, come mai?
		 */
	}
}
