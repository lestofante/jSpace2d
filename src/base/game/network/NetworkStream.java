package base.game.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.network.packets.PacketHandler;
import base.game.network.packets.TCP_Packet;

public class NetworkStream {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final int BUFFER_CAPACITY = 65000;
	private final SocketChannel in;
	private final ByteBuffer buffer;

	public final ConcurrentLinkedQueue<TCP_Packet> available;

	/**
	 * Class that makes our packets available to the game. You must assure the
	 * stream is in non-blocking mode, or the construction will fail
	 * 
	 * @param in
	 *            socket stream to monitor (must be in non blocking mode)
	 * @throws Exception
	 */
	public NetworkStream(SocketChannel in) throws Exception {
		if (!in.isBlocking()) {
			this.in = in;
			buffer = ByteBuffer.allocate(BUFFER_CAPACITY);
			available = new ConcurrentLinkedQueue<>();
		} else {
			throw new Exception("Non-blocking stream required");
		}
	}

	/**
	 * Updates available packets with contents from the stream
	 */
	public void update() {
		try {
			int read = getChannel().read(buffer);
			if (read != 0)
				log.debug("read {} bytes from {}", read, getChannel().getRemoteAddress());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buffer.flip();
		try {
			available.addAll(PacketHandler.getTCP(buffer, this));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SocketChannel getChannel() {
		return in;
	}

}
