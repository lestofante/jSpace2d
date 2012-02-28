package base.game.network;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import base.worker.Worker;

public abstract class NetworkHandler {

	public static int getNetworkMTU() {
		int out = Integer.MAX_VALUE;
		try {
			Enumeration<NetworkInterface> temp = NetworkInterface.getNetworkInterfaces();

			for (NetworkInterface tempNet : Collections.list(temp)) {
				int current = tempNet.getMTU();
				if (current < out)
					out = current;
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}

	protected final SelectorHandler sh;

	public NetworkHandler(SelectorHandler s) throws IOException {
		this.sh = s;
		sh.start();
	}

	public abstract void read(ArrayList<Worker> w);

	public ArrayList<Worker> update() throws IOException {
		return sh.update();
	}

	public abstract void write(ArrayList<Worker> wOUT);

}
