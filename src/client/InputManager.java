package client;

import java.util.List;

import org.lwjgl.input.Keyboard;

import base.game.network.packets.utils.ClientState.Gun;
import base.game.network.packets.utils.ClientState.Rotation;
import base.game.network.packets.utils.ClientState.Translation;
import client.worker.ClientWorker;
import client.worker.StateEntity;

public class InputManager {

	String myName;

	Translation t;
	Rotation r;
	Gun g;

	/*
	 * binding for key
	 */
	private final int NORTH = Keyboard.KEY_W;
	private final int SOUTH = Keyboard.KEY_S;
	private final int EAST = Keyboard.KEY_D;
	private final int WEST = Keyboard.KEY_A;

	private final int CLOCKWISE = Keyboard.KEY_E;
	private final int COUNTERCLOCKWISE = Keyboard.KEY_Q;

	private final int PRIMARY_FIRE = Keyboard.KEY_SPACE;
	private final int SECONDARY_FIRE = Keyboard.KEY_LMENU;

	public InputManager(String myName) {
		this.myName = myName;
	}

	public void update(List<ClientWorker> wIN) {
		t = Translation.STILL;
		r = Rotation.STILL;
		g = Gun.NO_FIRE;

		while (Keyboard.next()) {

			int eventKey = Keyboard.getEventKey();

			if (WEST == eventKey) {
				if (t == Translation.NORTH || t == Translation.NORTH_WEST)
					t = Translation.NORTH_WEST;
				else if (t == Translation.SOUTH || t == Translation.SOUTH_WEST)
					t = Translation.SOUTH_WEST;
				else
					t = Translation.WEST;
			} else if (EAST == eventKey) {
				if (t == Translation.NORTH || t == Translation.NORTH_EAST)
					t = Translation.NORTH_EAST;
				else if (t == Translation.SOUTH || t == Translation.SOUTH_EAST)
					t = Translation.SOUTH_EAST;
				else
					t = Translation.EAST;
				break;
			} else if (NORTH == eventKey) {
				if (t == Translation.EAST || t == Translation.NORTH_EAST)
					t = Translation.NORTH_EAST;
				else if (t == Translation.WEST || t == Translation.NORTH_WEST)
					t = Translation.NORTH_WEST;
				else
					t = Translation.NORTH;
				break;
			} else if (SOUTH == eventKey) {
				if (t == Translation.EAST || t == Translation.SOUTH_EAST)
					t = Translation.SOUTH_EAST;
				else if (t == Translation.WEST || t == Translation.SOUTH_WEST)
					t = Translation.SOUTH_WEST;
				else
					t = Translation.SOUTH;
				break;
			} else if (CLOCKWISE == eventKey) {
				r = Rotation.CLOCKWISE;
				break;
			} else if (COUNTERCLOCKWISE == eventKey) {
				r = Rotation.COUNTERCLOCKWISE;
				break;
			} else if (PRIMARY_FIRE == eventKey) {
				if (g.equals(Gun.SECONDARY_FIRE)) {
					g = Gun.TOGHEDER_FIRE;
				} else
					g = Gun.PRIMARY_FIRE;
				break;
			} else if (SECONDARY_FIRE == eventKey) {
				if (g.equals(Gun.PRIMARY_FIRE)) {
					g = Gun.TOGHEDER_FIRE;
				} else
					g = Gun.SECONDARY_FIRE;
				break;
			}

		}
		wIN.add(new StateEntity(myName, t, r, g));

	}

}
