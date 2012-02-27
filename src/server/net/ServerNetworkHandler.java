package server.net;

import java.io.IOException;


import base.game.network.NetworkHandler;

public class ServerNetworkHandler extends NetworkHandler {

	public ServerNetworkHandler() throws IOException {
		super( new ServerSelectorHandler(getNetworkMTU()) );
	}

}
