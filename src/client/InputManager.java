package client;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.network.packets.utils.ClientState.Gun;
import base.game.network.packets.utils.ClientState.Rotation;
import base.game.network.packets.utils.ClientState.Translation;
import client.worker.ClientWorker;

public class InputManager {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	String myName;

	private final boolean[] wasdArray = new boolean[4];
	private final boolean[] rotationArray = new boolean[2];
	private final boolean[] gunArray = new boolean[2];

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

	private Translation t = Translation.STILL;

	private Rotation r = Rotation.STILL;

	private Gun g = Gun.NO_FIRE;

	public InputManager(String myName) {
		this.myName = myName;
	}

	public void update(List<ClientWorker> wIN) {

		while (Keyboard.next()) {

			int eventKey = Keyboard.getEventKey();

			if (Keyboard.getEventKeyState())
				keyPressed(eventKey);
			else
				keyReleased(eventKey);

		}
		processTranlsation();
		processRotation();
		processGun();

		// wIN.add(new StateEntity(myName, t, r, g));
		wIN.add(new PlayerAction(t, r, g));
	}

	private void processTranlsation() {
		int leftRight_contribute = 0;
		int upDown_contribute = 0;

		if (wasdArray[0])
			leftRight_contribute += 1;
		if (wasdArray[2])
			leftRight_contribute -= 1;

		if (wasdArray[1])
			upDown_contribute += 1;
		if (wasdArray[3])
			upDown_contribute -= 1;

		// so now these 2 helper variables are 0 in case none or both keys are
		// pressed, and +1 or -1 if only one is pressed
		if (leftRight_contribute == 0) {
			if (upDown_contribute == 0) {
				t = Translation.STILL;
			} else if (upDown_contribute == 1) {
				t = Translation.NORTH;
			} else if (upDown_contribute == -1) {
				t = Translation.SOUTH;
			}
		} else if (leftRight_contribute == 1) {
			if (upDown_contribute == 0) {
				t = Translation.WEST;
			} else if (upDown_contribute == 1) {
				t = Translation.NORTH_WEST;
			} else if (upDown_contribute == -1) {
				t = Translation.SOUTH_WEST;
			}
		} else if (leftRight_contribute == -1) {
			if (upDown_contribute == 0) {
				t = Translation.EAST;
			} else if (upDown_contribute == 1) {
				t = Translation.NORTH_EAST;
			} else if (upDown_contribute == -1) {
				t = Translation.SOUTH_EAST;
			}
		}
	}

	private void processRotation() {
		if (rotationArray[0]) {
			if (rotationArray[1]) {
				r = Rotation.STILL;
			} else {
				r = Rotation.CLOCKWISE;
			}
		} else {
			if (rotationArray[1]) {
				r = Rotation.COUNTERCLOCKWISE;
			} else {
				r = Rotation.STILL;
			}
		}
	}

	private void processGun() {
		if (gunArray[0]) {
			if (gunArray[1]) {
				g = Gun.SIMULTANEOUS_FIRE;
			} else {
				g = Gun.PRIMARY_FIRE;
			}
		} else {
			if (gunArray[1]) {
				g = Gun.SECONDARY_FIRE;
			} else {
				g = Gun.NO_FIRE;
			}
		}
	}

	private void keyPressed(int eventKey) {
		if (WEST == eventKey) {
			wasdArray[0] = true;
			return;
		}
		if (EAST == eventKey) {
			wasdArray[2] = true;
			return;
		}
		if (NORTH == eventKey) {
			wasdArray[1] = true;
			return;
		}
		if (SOUTH == eventKey) {
			wasdArray[3] = true;
			return;
		}
		if (CLOCKWISE == eventKey) {
			rotationArray[0] = true;
			return;
		}
		if (COUNTERCLOCKWISE == eventKey) {
			rotationArray[1] = true;
			return;
		}
		if (PRIMARY_FIRE == eventKey) {
			gunArray[0] = true;
			return;
		}
		if (SECONDARY_FIRE == eventKey) {
			gunArray[1] = true;
			return;
		}
	}

	private void keyReleased(int eventKey) {
		if (WEST == eventKey) {
			wasdArray[0] = false;
			return;
		}
		if (EAST == eventKey) {
			wasdArray[2] = false;
			return;
		}
		if (NORTH == eventKey) {
			wasdArray[1] = false;
			return;
		}
		if (SOUTH == eventKey) {
			wasdArray[3] = false;
			return;
		}
		if (CLOCKWISE == eventKey) {
			rotationArray[0] = false;
			return;
		}
		if (COUNTERCLOCKWISE == eventKey) {
			rotationArray[1] = false;
			return;
		}
		if (PRIMARY_FIRE == eventKey) {
			gunArray[0] = false;
			return;
		}
		if (SECONDARY_FIRE == eventKey) {
			gunArray[1] = false;
			return;
		}
	}
}
