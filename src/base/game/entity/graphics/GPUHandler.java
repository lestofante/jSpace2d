package base.game.entity.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GLContext;

public class GPUHandler {

	public static void bufferData(int id, FloatBuffer buffer, int GLHint) {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
			ARBBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, buffer, GLHint);
			System.out.println("VBO buffering succeded!");
		} else {
			System.out.println("VBO buffering failed!");
			System.exit(0);
		}
	}

	public static void bufferElementData(int id, IntBuffer buffer, int GLHint) {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, id);
			ARBBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, buffer, GLHint);
			System.out.println("VBO buffering succeded!");
		} else {
			System.out.println("VBO buffering failed!");
			System.exit(0);
		}
	}

	public static int createVBOID() {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			IntBuffer buffer = BufferUtils.createIntBuffer(1);
			ARBBufferObject.glGenBuffersARB(buffer);
			System.out.println("VBO creation succeded! VBOID: " + buffer.get(0));
			return buffer.get(0);
		} else {
			System.out.println("VBO creation failed!");
			System.exit(0);
		}
		return 0;
	}
}
