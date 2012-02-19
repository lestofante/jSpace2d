package base.game.player.network;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import base.worker.Worker;

public class NetworkHandler {

	private final SelectorHandler sh;
	private int MTU;

	public NetworkHandler() throws IOException{
		this.MTU = getNetworkMTU();
		this.sh = new SelectorHandler(MTU);
		sh.startServer();
	}

	public static int getNetworkMTU(){		
		int out = Integer.MAX_VALUE;
		try {
			Enumeration<NetworkInterface> temp = NetworkInterface.getNetworkInterfaces();

			for(NetworkInterface tempNet : Collections.list(temp)){
				int current = tempNet.getMTU();
				if(current<out)
					out = current;
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}

	public ArrayList<ByteBuffer> writeRawActions() {
		ArrayList<ByteBuffer> out = new ArrayList<>();
		return out;
	}

	public ArrayList<Worker> update() throws IOException {
		return sh.update();
	}

}
