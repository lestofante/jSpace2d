package base.game.network.packets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class LoginPacket extends TCP_Packet {

	/*
	 * allora questo è puro dati estende un generico TCP_Packet e ne definisce
	 * la struttura ma n non capisco, se contiene il getDataBuffer perchè non il
	 * setDataBuffer?!?! cmq queste discussioni sono da salvare!
	 * 
	 * si allora il setDatabuffer non ha senso il pacchetto login, non può
	 * essere diverso, una volta settati username e shipiD, capito?
	 * 
	 * no, visione errata, IMHO. immagina che cambiamo qualcosa nei login, e che
	 * quindi ne esistano 2 versioni. si cambia questa classe o se ne crea
	 * un'altra certo, ma se vuoi mantenere la retrocompatibilità? in effetti è
	 * un problema che non ci tocca boh non stai parlando di un cambiamento da
	 * poco. cambiare il protocollo con cui client e server si parlano, è tipo
	 * la cosa più compromettente che si possa fare perchè vai implicitamente a
	 * cambiare entrambi se poi vuoi cmq lasciarti lo spazio per farlo...non so
	 * in effetti da questo punto di vista è più comodo vincolare il server e
	 * client ad avere la stessa versione, e fin quì ok però quando i nostri
	 * worker inizaino a crescere (pensa allo scaricamento o aggiornamento dei
	 * dati sulle mappe, texture etc..) si certo hai ragione, però in quel caso
	 * forse ci conviene aggrapparci ad altri metodi, come lo stream di oggetti,
	 * visto che non è proprio una questione di performance non so come
	 * spiegarti
	 * 
	 * facciamo così, vai avanti così, visto che per ora ci stai lavorando su.
	 * poi al massimo ci mettiamo poco a copiare i mtodi nelle rispettive classi
	 * e cambiarci nome :) ok allora, poi domenica di nuovo insieme però
	 */

	private final String username;
	private final byte shipID;

	public String getUsername() {
		return username;
	}

	public byte getShipID() {
		return shipID;
	}

	public LoginPacket(String userName, byte shipID) {
		super(PacketType.LOGIN);
		if (userName.length() > 30)
			userName = userName.substring(0, 30);
		this.username = userName;
		this.shipID = shipID;
	}

	@Override
	public ByteBuffer getDataBuffer() {
		ByteBuffer out = ByteBuffer.allocate(32);
		out.clear();
		out.put((byte) -127);

		int i = 0;
		try {
			for (; i < username.getBytes("ASCII").length; i++)
				out.put(username.getBytes("ASCII")[i]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		for (; i < 30; i++)
			out.put((byte) 32);
		out.put(shipID);
		out.flip();
		return out;
	}

}
