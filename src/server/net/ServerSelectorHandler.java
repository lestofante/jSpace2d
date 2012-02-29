package server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import base.game.network.SelectorHandler;
import base.game.network.packets.PacketRecognizer;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP_Packet.PacketType;
import base.game.player.worker.RemoveNetworkPlayer;
import base.worker.NetworkWorker;
import base.worker.Worker;

public class ServerSelectorHandler implements SelectorHandler {

	Selector connected = null;
	private final int MTU;

	public ServerSelectorHandler(int MTU) {
		this.MTU = MTU;
	}

	private ServerSocketChannel createServerTCPSocketChannel(int port) {
		try {
			ServerSocketChannel listener = ServerSocketChannel.open();
			listener.configureBlocking(false);
			listener.socket().bind(new InetSocketAddress(port));
			return listener;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private RemoveNetworkPlayer disconnectAndRemove(SelectionKey key) {
		try {
			key.channel().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		key.cancel();

		System.out.println("Disconnected player: " + key.attachment());

		return new RemoveNetworkPlayer(key);
	}

	private ArrayList<Worker> lookForInput() {
		ArrayList<Worker> ret = new ArrayList<>();
		try {
			connected.selectNow();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Set<SelectionKey> selectedKeys = connected.selectedKeys();

		Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

		NetworkWorker input;
		SelectionKey key;

		while (keyIterator.hasNext()) {
			key = keyIterator.next();
			if (key.isReadable() && key.isValid()) {
				try {
					input = readWorker(key);
					if (input != null)
						ret.add(input);
				} catch (IOException e) {
					ret.add(disconnectAndRemove(key));
					e.printStackTrace();
				} catch (Exception e) {
					ret.add(disconnectAndRemove(key));
					e.printStackTrace();
				}
			}
			keyIterator.remove();
		}

		return ret;
	}

	private NetworkWorker readWorker(SelectionKey key) throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(MTU);
		buf.clear();
		SocketChannel channel = (SocketChannel) key.channel();
		int numBytesRead = channel.read(buf);

		if (numBytesRead == -1) {
			throw new Exception("Error reading from channel");
		}

		buf.flip();
		TCP_Packet packet = null;

		try {
			packet = PacketRecognizer.getTCP(buf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (packet != null) {
			if (packet.packetType != PacketType.LOGIN) {
				// TODO implement packets
			} else {
				throw new Exception("Address already logged in");
			}
		} else {
			throw new Exception("Error handling packet");
		}

		return null;

	}

	@Override
	public boolean start() throws IOException {
		// Create the selectors
		connected = Selector.open();
		return true;
	}

	@Override
	public ArrayList<Worker> update() throws IOException {
		return lookForInput();
	}

	public void addConnectedChannel(SocketChannel newLogin, String playerName) throws ClosedChannelException {
		newLogin.register(connected, SelectionKey.OP_READ | SelectionKey.OP_WRITE, playerName);
		try {
			System.out.println("New channel connected: " + newLogin.getRemoteAddress());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
