package base.graphics.object;

import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;

public class VBORenderable extends GameRenderable {

	private int verticesBufferID;
	private int normalsBufferID;
	private int triangleCount;

	public VBORenderable(int vertexBufferID, int normalBufferID, int triangleCount, float[] transform) {
		super(transform);
		this.verticesBufferID = vertexBufferID;
		this.normalsBufferID = normalBufferID;
		this.triangleCount = triangleCount;
	}

	@Override
	public void render() {

		GL11.glPushMatrix();

		GL11.glTranslatef(transform[0], transform[1], 0.0f);
		GL11.glRotatef((float) Math.toDegrees(transform[2]), 0, 0, 1);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, verticesBufferID);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, normalsBufferID);
		GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0);

		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, triangleCount * 3);

		GL11.glPopMatrix();

	}

	@Override
	public void renderInterleavedDrawArray() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
