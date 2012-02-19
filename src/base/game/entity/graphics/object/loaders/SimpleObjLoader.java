package base.game.entity.graphics.object.loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.lwjgl.util.vector.Vector3f;

import base.game.entity.graphics.object.common.Triangle;

public class SimpleObjLoader {

	public static ArrayList<Triangle> loadGeometry(Path pathToObject) {
		// load all the file
		ArrayList<String> lines = new ArrayList<String>();

		Charset charset = Charset.forName("US-ASCII");
		try (BufferedReader reader = Files.newBufferedReader(pathToObject, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

		// now let's process it
		StringTokenizer tokenizer;
		ArrayList<Vector3f> verticesList = new ArrayList<Vector3f>();
		ArrayList<Vector3f> normalsList = new ArrayList<Vector3f>();
		ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		for (String line : lines) {
			if (!line.startsWith("#")) {
				if (line.startsWith("v ")) {
					tokenizer = new StringTokenizer(line);
					tokenizer.nextToken();
					float[] coordinates = new float[3];
					for (int i = 0; i < 3; i++)
						coordinates[i] = Float.parseFloat(tokenizer.nextToken());
					verticesList.add(new Vector3f(coordinates[0], coordinates[1], coordinates[2]));
				}
				if (line.startsWith("vn ")) {
					tokenizer = new StringTokenizer(line);
					tokenizer.nextToken();
					float[] coordinates = new float[3];
					for (int i = 0; i < 3; i++)
						coordinates[i] = Float.parseFloat(tokenizer.nextToken());
					normalsList.add(new Vector3f(coordinates[0], coordinates[1], coordinates[2]));
				}
				if (line.startsWith("f ")) {
					tokenizer = new StringTokenizer(line);
					tokenizer.nextToken();
					int[] vertex = new int[3];
					int[] normal = new int[3];
					for (int i = 0; i < 3; i++) {
						String temp = tokenizer.nextToken();
						StringTokenizer tokenizer2 = new StringTokenizer(temp, "/ /", false);
						vertex[i] = Integer.parseInt(tokenizer2.nextToken()) - 1;
						normal[i] = Integer.parseInt(tokenizer2.nextToken()) - 1;
					}
					Triangle tempTriangle = new Triangle(verticesList.get(vertex[0]), verticesList.get(vertex[1]), verticesList.get(vertex[2]));
					tempTriangle.setNormals(normalsList.get(normal[0]), normalsList.get(normal[1]), normalsList.get(normal[2]));
					triangles.add(tempTriangle);
				}
			}
		}
		return triangles;
	}
}
