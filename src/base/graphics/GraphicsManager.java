package base.graphics;

import java.nio.FloatBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.common.AsyncActionBus;
import base.graphics.actions.G_CreateGameRenderableAction;
import base.graphics.actions.G_FollowObjectWithCamera;
import base.graphics.actions.G_RemoveGameRenderable;
import base.graphics.actions.GraphicAction;
import base.graphics.object.Camera;
import base.graphics.object.GameRenderable;

public class GraphicsManager implements Runnable {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private int fps;
	private final boolean fullScreen;
	private long lastFPS;
	private long lastFrame;
	private final DisplayMode mode;
	private FloatBuffer pos;
	private final HashMap<Integer, GameRenderable> toDraw = new HashMap<Integer, GameRenderable>();
	private ArrayList<GraphicAction> toProcess = new ArrayList<GraphicAction>();
	private final boolean vSync;

	private Camera camera;
	private ObjectHandler oHandler;

	private final AsyncActionBus asyncActionBus;
	private InputKeyboard inputHandler;

	/**
	 * Manages graphics.
	 * 
	 * @param mode
	 *            DisplayMode to set
	 * @param fullscreen
	 *            true to enable, false not to
	 * @param vSync
	 *            true to enable, false not to
	 */

	public GraphicsManager(DisplayMode mode, boolean fullScreen, boolean vSync, AsyncActionBus asyncActionBus) {
		this.asyncActionBus = asyncActionBus;
		this.mode = mode;
		this.fullScreen = fullScreen;
		this.vSync = vSync;
	}

	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	/**
	 * Get the time in milliseconds
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Initialize the screen, camera and light.
	 * 
	 * @param mode
	 *            DisplayMode to set
	 * @param fullscreen
	 *            true to enable, false not to
	 * @param vSync
	 *            true to enable, false not to
	 */

	// TODO separate camera in at least a different function, preferably in a
	// different class.

	private void init(DisplayMode mode, boolean fullScreen, boolean vSync) {

		// load objects
		oHandler = new ObjectHandler(Paths.get("Resources/Objects"));

		try {
			Display.setDisplayMode(mode);
			Display.setFullscreen(fullScreen);
			Display.setVSyncEnabled(vSync);
			Display.create();

		} catch (Exception e) {
			log.error("Error setting up display");
			System.exit(-1);
		}

		asyncActionBus.graphicsStarted.set(true);

		this.camera = new Camera();

		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();

		camera.perspective(25.0f, (float) width / (float) height, 1, 1000);

		lastFPS = getTime(); // call before loop to initialise fps timer

		GL11.glEnable(GL11.GL_DEPTH_TEST);

		float mat_specular[] = { 1.0f, 1.0f, 1.0f, 0.1f };
		float light_position[] = { 0.0f, 1.0f, 1.0f, 0.0f };
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glShadeModel(GL11.GL_SMOOTH);

		FloatBuffer spec = BufferUtils.createFloatBuffer(4).put(mat_specular);
		spec.flip();
		pos = BufferUtils.createFloatBuffer(4).put(light_position);
		pos.rewind();

		camera.position = new Vector3f(0, 0, 130);

		camera.lookAt(0, 0, 0);

		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, pos);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

	}

	protected void processActions() {
		toProcess = asyncActionBus.getGraphicActions();
		for (GraphicAction action : toProcess) {

			switch (action.actionType) {

			case CREATE:
				GameRenderable temp = toDraw.get(((G_CreateGameRenderableAction) action).iD);

				if (temp == null) {
					log.debug("Creating graphical object with ID: {}", ((G_CreateGameRenderableAction) action).iD);
					GameRenderable tempRenderable = null;
					try {
						tempRenderable = oHandler.requestVBOMesh(((G_CreateGameRenderableAction) action).modelName, ((G_CreateGameRenderableAction) action).transform);
					} catch (Exception e) {
						log.error("Error loading VBOMesh", e);
						System.exit(-1);
					}
					toDraw.put(((G_CreateGameRenderableAction) action).iD, tempRenderable);
				} else {
					try {
						throw new Exception();
					} catch (Exception e) {
						log.error("Object ID already present: {}", ((G_CreateGameRenderableAction) action).iD, e);
						System.exit(-1);
					}
				}
				break;

			case REMOVE:
				temp = toDraw.get(((G_RemoveGameRenderable) action).iD);

				if (temp != null) {
					log.debug("Removing graphical obejct with ID: {}", ((G_RemoveGameRenderable) action).iD);
					toDraw.remove(((G_RemoveGameRenderable) action).iD);
				} else {
					try {
						throw new Exception();
					} catch (Exception e) {
						log.error("Object ID does not exist: {}", ((G_RemoveGameRenderable) action).iD, e);
						System.exit(-1);
					}
				}
				break;

			case FOLLOW_OBJECT:
				camera.sharedTransform = ((G_FollowObjectWithCamera) action).transform;
			}

		}
	}

	private void render() {
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, pos);

		asyncActionBus.sharedLock.readLock().lock();
		camera.update();

		for (GameRenderable renderable : toDraw.values()) {

			renderable.render();

		}
		asyncActionBus.sharedLock.readLock().unlock();

		// GL11.glFlush();
		GL11.glFinish();
		updateFPS();
	}

	@Override
	public void run() {

		init(mode, fullScreen, vSync);

		inputHandler = new InputKeyboard(asyncActionBus);

		while (!Display.isCloseRequested()) {
			update();
			Display.sync(60);
		}

		asyncActionBus.graphicsStarted.set(false);

		Display.destroy();

		// log.debug("Display closed");

		System.out.println("Display closed");
		System.out.flush();

	}

	public void update() {
		processActions();
		inputHandler.update();
		render();
		Display.update();
	}

	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			System.out.println("fps: " + fps);
			fps = 0; // reset the FPS counter
			lastFPS += 1000; // add one second
		}
		fps++;
	}

}
