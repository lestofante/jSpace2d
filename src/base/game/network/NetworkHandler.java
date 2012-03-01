package base.game.network;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.worker.Worker;

public abstract class NetworkHandler {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

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

	public abstract void read(ArrayList<Worker> w);

	public abstract void write(ArrayList<Worker> wOUT);

}
