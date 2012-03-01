package base.graphics.object.common;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

public class Mesh {

	public final ArrayList<Triangle> triangles;
	public final String modelName;
	public final FloatBuffer verticesBuffer;
	public final FloatBuffer normalsBuffer;
	public final FloatBuffer interleavedBuffer;
	public boolean VBOInitialized;
	public int vertexVBOID;
	public int normalVBOID;

	public Mesh(ArrayList<Triangle> triangles, String modelName) {
		this.triangles = triangles;
		this.modelName = modelName;
		verticesBuffer = BufferUtils.createFloatBuffer(triangles.size() * 9);
		normalsBuffer = BufferUtils.createFloatBuffer(triangles.size() * 9);
		interleavedBuffer = BufferUtils.createFloatBuffer(triangles.size() * 9 * 2);
		createBuffers();
	}

	private void createBuffers() {

		// fill Buffers
		Vector3f[] faceVertices = null;
		Vector3f[] faceNormals = null;

		for (int i = 0; i < triangles.size(); i++) {

			faceVertices = triangles.get(i).vertices;
			faceNormals = triangles.get(i).normals;

			verticesBuffer.put(faceVertices[0].x).put(faceVertices[0].y).put(faceVertices[0].z);
			verticesBuffer.put(faceVertices[1].x).put(faceVertices[1].y).put(faceVertices[1].z);
			verticesBuffer.put(faceVertices[2].x).put(faceVertices[2].y).put(faceVertices[2].z);

			normalsBuffer.put(faceNormals[0].x).put(faceNormals[0].y).put(faceNormals[0].z);
			normalsBuffer.put(faceNormals[1].x).put(faceNormals[1].y).put(faceNormals[1].z);
			normalsBuffer.put(faceNormals[2].x).put(faceNormals[2].y).put(faceNormals[2].z);

			interleavedBuffer.put(faceNormals[0].x).put(faceNormals[0].y).put(faceNormals[0].z); // normal
			// for
			// vertex
			// 1
			interleavedBuffer.put(faceVertices[0].x).put(faceVertices[0].y).put(faceVertices[0].z); // vertex
			// 1

			interleavedBuffer.put(faceNormals[1].x).put(faceNormals[1].y).put(faceNormals[1].z); // normal
			// for
			// vertex
			// 2
			interleavedBuffer.put(faceVertices[1].x).put(faceVertices[1].y).put(faceVertices[1].z); // vertex
			// 2

			interleavedBuffer.put(faceNormals[2].x).put(faceNormals[2].y).put(faceNormals[2].z); // normal
			// for
			// vertex
			// 3
			interleavedBuffer.put(faceVertices[2].x).put(faceVertices[2].y).put(faceVertices[2].z); // vertex
			// 3
		}

		// set ready to be read
		verticesBuffer.flip();
		normalsBuffer.flip();
		interleavedBuffer.flip();

	}

}
