package client;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.common.AsyncActionBus;
import base.game.network.packets.utils.ClientState.Gun;
import base.game.network.packets.utils.ClientState.Rotation;
import base.game.network.packets.utils.ClientState.Translation;
import client.worker.ClientWorker;

public class InputManager {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

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

	private final AsyncActionBus bus;

	public InputManager(AsyncActionBus bus) {
		this.bus = bus;
	}

	public void update(List<ClientWorker> wIN) {

		processTranlsation();
		processRotation();
		processGun();

		// wIN.add(new StateEntity(myName, t, r, g));
		wIN.add(new PlayerAction(t, r, g));
	}

	private void processTranlsation() {
		AtomicBoolean wasdArray[] = bus.getWasdArray();

		int leftRight_contribute = 0;
		int upDown_contribute = 0;

		if (wasdArray[0].get())
			leftRight_contribute += 1;
		if (wasdArray[2].get())
			leftRight_contribute -= 1;

		if (wasdArray[1].get())
			upDown_contribute += 1;
		if (wasdArray[3].get())
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
		AtomicBoolean rotationArray[] = bus.getRotationArray();
		if (rotationArray[0].get()) {
			if (rotationArray[1].get()) {
				r = Rotation.STILL;
			} else {
				r = Rotation.CLOCKWISE;
			}
		} else {
			if (rotationArray[1].get()) {
				r = Rotation.COUNTERCLOCKWISE;
			} else {
				r = Rotation.STILL;
			}
		}
	}

	private void processGun() {
		AtomicBoolean gunArray[] = bus.getGunArray();
		if (gunArray[0].get()) {
			if (gunArray[1].get()) {
				g = Gun.SIMULTANEOUS_FIRE;
			} else {
				g = Gun.PRIMARY_FIRE;
			}
		} else {
			if (gunArray[1].get()) {
				g = Gun.SECONDARY_FIRE;
			} else {
				g = Gun.NO_FIRE;
			}
		}
	}
}
