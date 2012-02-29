package server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import base.game.network.Login;
import base.game.network.NetworkHandler;
import base.game.network.packets.LoginPacket;
import base.game.network.packets.PacketRecognizer;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP_Packet.PacketType;
import base.worker.Worker;

public class ServerNetworkHandler extends NetworkHandler {

	private final ServerSocketChannel listenerChannel;

	// private final LoginHandler loginHandler;

	public ServerNetworkHandler() throws IOException {
		super(new ServerSelectorHandler(getNetworkMTU()));

		listenerChannel = ServerSocketChannel.open();
		listenerChannel.configureBlocking(false);
		listenerChannel.bind(new InetSocketAddress(9999));

	}

	@Override
	public void read(ArrayList<Worker> w) {
		try {
			update(w);
			w.addAll(sh.update());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void write(ArrayList<Worker> wOUT) {
		// TODO Auto-generated method stub

	}

	@Override
	protected final void update(ArrayList<Worker> w) throws IOException {

		try {
			Login newLogin = checkForLoginRequests(w);
			if (newLogin != null)
				((ServerSelectorHandler) sh).addConnectedChannel(newLogin.getChannel(), newLogin.getUsername());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Login checkForLoginRequests(ArrayList<Worker> out) throws Exception {
		SocketChannel accept = listenerChannel.accept();
		Login wLogin = null;

		if (accept != null) {
			accept.configureBlocking(false);
			ByteBuffer dst = ByteBuffer.allocate(32);
			dst.clear();
			accept.read(dst);
			dst.flip();
			TCP_Packet packet = null;

			try {
				packet = PacketRecognizer.getTCP(dst);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (packet != null) {
				if (packet.packetType == PacketType.LOGIN) {
					wLogin = new Login((LoginPacket) packet);
					wLogin.setChannel(accept);
					out.add(wLogin);
					System.out.println("Received a login from address: " + accept.getRemoteAddress() + " with username: " + ((LoginPacket) packet).getUsername());
				} else {
					throw new Exception("Not a login packet");
				}
			} else {
				throw new Exception("Error handling packet");
			}

		}

		return wLogin;
	}
}
