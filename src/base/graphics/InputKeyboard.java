package base.graphics;

import java.util.concurrent.atomic.AtomicBoolean;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.common.AsyncActionBus;
import base.game.network.packets.utils.ClientState.Gun;
import base.game.network.packets.utils.ClientState.Rotation;
import base.game.network.packets.utils.ClientState.Translation;

public class InputKeyboard {
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

	private final Translation t = Translation.STILL;

	private final Rotation r = Rotation.STILL;

	private final Gun g = Gun.NO_FIRE;

	private final AsyncActionBus bus;

	public InputKeyboard(AsyncActionBus bus) {
		this.bus = bus;
	}

	public void update() {

		while (Keyboard.next()) {

			int eventKey = Keyboard.getEventKey();

			if (Keyboard.getEventKeyState())
				keyPressed(eventKey);
			else
				keyReleased(eventKey);

		}
	}

	private void keyPressed(int eventKey) {
		AtomicBoolean wasdArray[] = bus.getWasdArray();
		AtomicBoolean rotationArray[] = bus.getWasdArray();
		AtomicBoolean gunArray[] = bus.getWasdArray();

		if (WEST == eventKey) {
			wasdArray[0].set(true);
			return;
		}
		if (EAST == eventKey) {
			wasdArray[2].set(true);
			return;
		}
		if (NORTH == eventKey) {
			wasdArray[1].set(true);
			return;
		}
		if (SOUTH == eventKey) {
			wasdArray[3].set(true);
			return;
		}
		if (CLOCKWISE == eventKey) {
			rotationArray[0].set(true);
			return;
		}
		if (COUNTERCLOCKWISE == eventKey) {
			rotationArray[1].set(true);
			return;
		}
		if (PRIMARY_FIRE == eventKey) {
			gunArray[0].set(true);
			return;
		}
		if (SECONDARY_FIRE == eventKey) {
			gunArray[1].set(true);
			return;
		}
	}

	private void keyReleased(int eventKey) {
		AtomicBoolean wasdArray[] = bus.getWasdArray();
		AtomicBoolean rotationArray[] = bus.getWasdArray();
		AtomicBoolean gunArray[] = bus.getWasdArray();

		if (WEST == eventKey) {
			wasdArray[0].set(false);
			return;
		}
		if (EAST == eventKey) {
			wasdArray[2].set(false);
			return;
		}
		if (NORTH == eventKey) {
			wasdArray[1].set(false);
			return;
		}
		if (SOUTH == eventKey) {
			wasdArray[3].set(false);
			return;
		}
		if (CLOCKWISE == eventKey) {
			rotationArray[0].set(false);
			return;
		}
		if (COUNTERCLOCKWISE == eventKey) {
			rotationArray[1].set(false);
			return;
		}
		if (PRIMARY_FIRE == eventKey) {
			gunArray[0].set(false);
			return;
		}
		if (SECONDARY_FIRE == eventKey) {
			gunArray[1].set(false);
			return;
		}
	}
}
