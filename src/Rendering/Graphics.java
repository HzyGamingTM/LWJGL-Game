package Rendering;

import Utils.OurMath;

import io.Input;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Graphics {
	public static class Vertex {
		public OurMath.Vector3 pos, color;
		public OurMath.Vector2 textureCoord;

		public Vertex(OurMath.Vector3 pos, OurMath.Vector3 color, OurMath.Vector2 textureCoord) {
			this.pos = pos;
			this.color = color;
			this.textureCoord = textureCoord;
		}
	}

	public static class Mesh {
		public Vertex[] vertices;
		public int[] indices;
		public Material material;
		public int vao, pbo, ibo, cbo, tbo;

		public Mesh(Vertex[] vertices, int[] indices, Material material) {
			this.vertices = vertices;
			this.indices = indices;
			this.material = material;
		}

		public void create() {
			material.create();

			vao = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vao);

			FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
			float[] positionData = new float[vertices.length * 3];
			for (int i = 0; i < vertices.length; i++) {
				positionData[i * 3] = vertices[i].pos.x;
				positionData[i * 3 + 1] = vertices[i].pos.y;
				positionData[i * 3 + 2] = vertices[i].pos.z;
			}
			positionBuffer.put(positionData).flip();

			pbo = storeData(positionBuffer, 0, 3);

			FloatBuffer colorBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
			float[] colorData = new float[vertices.length * 3];
			for (int i = 0; i < vertices.length; i++) {
				colorData[i * 3] = vertices[i].color.x;
				colorData[i * 3 + 1] = vertices[i].color.y;
				colorData[i * 3 + 2] = vertices[i].color.z;
			}
			colorBuffer.put(colorData).flip();

			cbo = storeData(colorBuffer, 1, 3);

			FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(vertices.length * 2);
			float[] textureData = new float[vertices.length * 2];
			for (int i = 0; i < vertices.length; i++) {
				textureData[i * 2] = vertices[i].textureCoord.x;
				textureData[i * 2 + 1] = vertices[i].textureCoord.y;
			}
			textureBuffer.put(textureData).flip();

			tbo = storeData(textureBuffer, 2, 2);

			IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			ibo = GL30.glGenBuffers();

			GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ibo);
			GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL30.GL_STATIC_DRAW);
			GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, 0);
		}

		private int storeData(FloatBuffer buffer, int index, int size) {
			int bufferID = GL30.glGenBuffers();
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, bufferID);
			GL30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
			GL30.glVertexAttribPointer(index, size, GL30.GL_FLOAT, false, 0, 0);
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
			return bufferID;
		}

		public void destroy() {
			GL30.glDeleteBuffers(pbo);
			GL30.glDeleteBuffers(cbo);
			GL30.glDeleteBuffers(ibo);
			GL30.glDeleteBuffers(tbo);

			GL30.glDeleteVertexArrays(vao);
			material.destroy();
		}
	}
}