package base.graphics;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.ARBBufferObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.graphics.object.RAMRenderable;
import base.graphics.object.VBORenderable;
import base.graphics.object.common.Mesh;
import base.graphics.object.common.Triangle;
import base.graphics.object.loaders.SimpleObjLoader;

public class ObjectHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final HashMap<String, Mesh> models = new HashMap<>();;
	private final Path modelLocation;

	public ObjectHandler(Path modelLocation) {
		this.modelLocation = modelLocation;
		init();
	}

	private Mesh getMesh(String modelName) throws Exception {
		Mesh out = models.get(modelName);

		if (out == null) {
			throw new Exception("Graphical model does not exist: " + modelName);
		}

		return out;
	}

	private void init() {
		log.debug("Loading graphical models");
		ArrayList<Path> modelPaths = searchForModels();

		Mesh temp;
		for (Path pathToModel : modelPaths) {
			temp = loadMesh(pathToModel);
			models.put(pathToModel.getFileName().toString(), temp);
		}
		log.info("Loaded graphical models");
	}

	private Mesh loadMesh(Path pathToModel) {
		log.debug("Loading: {}", pathToModel.toString());
		ArrayList<Triangle> triangles = SimpleObjLoader.loadGeometry(pathToModel);

		log.debug("Loaded: {} with {} triangles", pathToModel.getFileName().toString(), triangles.size());

		Mesh out = new Mesh(triangles, pathToModel.getFileName().toString());
		return out;
	}

	public RAMRenderable requestRAMMesh(String modelName, float[] transform) throws Exception {
		Mesh temp = getMesh(modelName);
		RAMRenderable out = new RAMRenderable(temp.verticesBuffer, temp.normalsBuffer, temp.interleavedBuffer, transform);
		return out;
	}

	public VBORenderable requestVBOMesh(String modelName, float[] transform) throws Exception {
		Mesh temp = getMesh(modelName);
		if (!temp.VBOInitialized) {
			temp.vertexVBOID = GPUHandler.createVBOID();
			GPUHandler.bufferData(temp.vertexVBOID, temp.verticesBuffer, ARBBufferObject.GL_STATIC_DRAW_ARB);

			temp.normalVBOID = GPUHandler.createVBOID();
			GPUHandler.bufferData(temp.normalVBOID, temp.normalsBuffer, ARBBufferObject.GL_STATIC_DRAW_ARB);

			VBORenderable out = new VBORenderable(temp.vertexVBOID, temp.normalVBOID, temp.triangles.size(), transform);
			temp.VBOInitialized = true;
			return out;
		} else {
			VBORenderable out = new VBORenderable(temp.vertexVBOID, temp.normalVBOID, temp.triangles.size(), transform);
			return out;
		}
	}

	private ArrayList<Path> searchForModels() {
		ArrayList<Path> out = new ArrayList<>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(modelLocation)) {
			for (Path file : stream) {
				String temp = file.getFileName().toString();
				if (temp.endsWith(".obj")) {
					out.add(file);
				}
			}
		} catch (IOException | DirectoryIteratorException x) {
			// IOException can never be thrown
			// by the iteration. In this snippet,
			// it can only be thrown by
			// newDirectoryStream.
			System.err.println(x);
		}

		return out;
	}

}
