package base.game.entity.graphics.object.common;

import org.lwjgl.util.vector.Vector3f;

public final class Triangle {

	public Vector3f faceNormal = new Vector3f();
	public Vector3f[] normals = new Vector3f[3];
	public Vector3f[] vertices = new Vector3f[3];

	public Triangle(Vector3f _1, Vector3f _2, Vector3f _3) {
		vertices[0] = _1;
		vertices[1] = _2;
		vertices[2] = _3;
	}

	private void calculateFaceNormal() {
		float avgX = normals[0].x + normals[1].x + normals[2].x;
		float avgY = normals[1].y + normals[1].y + normals[2].y;
		float avgZ = normals[2].z + normals[2].z + normals[2].z;
		faceNormal.x = avgX / 3f;
		faceNormal.y = avgY / 3f;
		faceNormal.z = avgZ / 3f;
	}

	public void setNormals(Vector3f _1, Vector3f _2, Vector3f _3) {
		normals[0] = _1;
		normals[1] = _2;
		normals[2] = _3;
		calculateFaceNormal();
	}

	@Override
	public String toString() {
		System.out.println("\nTriangle");
		for (int i = 0; i < 3; i++) {
			System.out.println("Vertex " + i + " location: " + vertices[i]);
		}
		return "Triangle printed";
	}
}
