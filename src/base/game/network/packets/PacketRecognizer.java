package base.game.network.packets;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.network.packets.TCP.ClientActionPacket;
import base.game.network.packets.TCP.LoginPacket;
import base.game.network.packets.TCP.PlayRequestPacket;
import base.game.network.packets.TCP.UpdateMapPacket;

public class PacketRecognizer {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(base.game.network.packets.PacketRecognizer.class);

	public static TCP_Packet getTCP(ByteBuffer in) throws Exception {
		boolean enoughtByteToRead = in.hasRemaining();
		while(enoughtByteToRead){
			byte read =in.get();  
			switch(read){
				case 0:
					System.out.println("packet UpdateMapPacket readed: "+read+" "+(read & 0xFF));
					if ( !createLoginPacket(in) ){
						//underflow error, add back packet type, and terminate reading cicle because we don't have enought data
						in.position(in.position()-1);
						in.put( read );
						enoughtByteToRead=false;
					}
					break;
				case 1:
					System.out.println("packet UpdateMapPacket readed: "+read+" "+(read & 0xFF));
					if ( !createPlayRequestPacket(in) ){
						//underflow error, add back packet type, and terminate reading cicle because we don't have enought data
						in.position(in.position()-1);
						in.put( read );
						enoughtByteToRead=false;
					}
					break;
				case 2:
					System.out.println("packet UpdateMapPacket readed: "+read+" "+(read & 0xFF));
					if ( !createClientActionPacket(in) ){
						//underflow error, add back packet type, and terminate reading cicle because we don't have enought data
						in.position(in.position()-1);
						in.put( read );
						enoughtByteToRead=false;
					}
					break;
				case 3:
					System.out.println("packet UpdateMapPacket readed: "+read+" "+(read & 0xFF));
					if ( !createUpdateMapPacket(in) ){
						//underflow error, add back packet type, and terminate reading cicle because we don't have enought data
						in.position(in.position()-1);
						in.put( read );
						enoughtByteToRead=false;
					}
					break;
				default:
					log.error("readed: "+read);
					//put it back?!
					in.position(in.position()-1);
					in.put( read );
			}
			
			if (!in.hasRemaining()){
				//if the buffer is over without throwing any error (over any expectation)
				enoughtByteToRead=false;
				throw new Exception("Uknown packet type");
			}
		}
	}

	private static TCP_Packet createUpdateMapPacket(ByteBuffer in) {
		return new UpdateMapPacket(in);
	}

	private static TCP_Packet createClientActionPacket(ByteBuffer in) {
		return new ClientActionPacket(in);
	}

	private static TCP_Packet createPlayRequestPacket(ByteBuffer in) {
		return new PlayRequestPacket(in);
	}

	private static LoginPacket createLoginPacket(ByteBuffer in) {
		return new LoginPacket(in);
	}
}
