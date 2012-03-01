package base.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GPUHandler {

	public static final Logger log = LoggerFactory.getLogger(base.graphics.GPUHandler.class);

	public static void bufferData(int id, FloatBuffer buffer, int GLHint) {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
			ARBBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, buffer, GLHint);
			log.debug("VBO buffering succeded");
		} else {
			try {
				throw new Exception();
			} catch (Exception e) {
				log.error("VBO buffering failed", e);
			}
			System.exit(-1);
		}
	}

	public static void bufferElementData(int id, IntBuffer buffer, int GLHint) {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, id);
			ARBBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, buffer, GLHint);
			log.debug("VBO buffering succeded");
		} else {
			try {
				throw new Exception();
			} catch (Exception e) {
				log.error("VBO buffering failed", e);
			}
			System.exit(-1);
		}
	}

	public static int createVBOID() {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			IntBuffer buffer = BufferUtils.createIntBuffer(1);
			ARBBufferObject.glGenBuffersARB(buffer);
			log.debug("VBO creation succeded. VBOID: {}", buffer.get(0));
			return buffer.get(0);
		} else {
			try {
				throw new Exception();
			} catch (Exception e) {
				log.error("VBO creation failed", e);
			}
			System.exit(-1);
		}
		return -1;
	}
}
