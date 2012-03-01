package base.graphics.object;

public abstract class GameRenderable {

	public float[] transform;

	public GameRenderable(float[] transform) {
		this.transform = transform;
	}

	public abstract void render();

	public abstract void renderInterleavedDrawArray();

}
