package base.graphics.object;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	public float[] sharedTransform;
	public Vector3f position;
	public Vector3f lookAt;
	public Vector3f upVector;

	public Camera() {
		this(new Vector3f(), new Vector3f(), new Vector3f());
	}

	public Camera(Vector3f position, Vector3f lookAt, Vector3f upVector) {
		this.position = position;
		this.lookAt = lookAt;
		this.upVector = new Vector3f(0, 1, 0);

		this.sharedTransform = new float[3];
	}

	public void lookAt(int i, int j, int k) {
		lookAt.set(i, j, k);
	}

	public void perspective(float fov, float g, int i, int j) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(fov, g, i, j);
	}

	public void update() {
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		position.x = sharedTransform[0];
		position.y = sharedTransform[1];

		upVector.x = (float) Math.sin(-sharedTransform[2]);
		upVector.y = (float) Math.cos(-sharedTransform[2]);

		GLU.gluLookAt(position.x, position.y, position.z, position.x, position.y, 0, upVector.x, upVector.y, upVector.z);
	}
}
