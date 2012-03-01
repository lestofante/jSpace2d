package base.graphics.object;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

public class RAMRenderable extends GameRenderable {

	protected FloatBuffer normalsBuffer;
	protected FloatBuffer verticesBuffer;
	protected FloatBuffer interleavedBuffer;

	private RAMRenderable(FloatBuffer verticesBuffer, FloatBuffer normalsBuffer, FloatBuffer interleavedBuffer) {
		super(new float[3]);
		setBuffers(verticesBuffer, normalsBuffer, interleavedBuffer);
	}

	public RAMRenderable(FloatBuffer verticesBuffer, FloatBuffer normalsBuffer, FloatBuffer interleavedBuffer, float[] transform) {
		super(transform);
		setBuffers(verticesBuffer, normalsBuffer, interleavedBuffer);
	}

	@Override
	public void render() {

		GL11.glPushMatrix();

		verticesBuffer.rewind();

		GL11.glTranslatef(transform[0], transform[1], 0.0f);
		GL11.glRotatef((float) Math.toDegrees(transform[2]), 0, 0, 1);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glVertexPointer(3, 0, verticesBuffer);
		GL11.glNormalPointer(0, normalsBuffer);

		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, verticesBuffer.capacity() / 3);

		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);

		GL11.glPopMatrix();

	}

	@Override
	public void renderInterleavedDrawArray() {

		GL11.glPushMatrix();

		GL11.glTranslatef(transform[0], transform[1], 0.0f);
		GL11.glRotatef((float) Math.toDegrees(transform[2]), 0, 0, 1);

		GL11.glInterleavedArrays(GL11.GL_N3F_V3F, 0, interleavedBuffer);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, interleavedBuffer.capacity() / 6);

		GL11.glPopMatrix();

	}

	public void setBuffers(FloatBuffer verticesBuffer, FloatBuffer normalsBuffer, FloatBuffer interleavedBuffer) {

		this.verticesBuffer = verticesBuffer;
		this.normalsBuffer = normalsBuffer;
		this.interleavedBuffer = interleavedBuffer;

	}
}
