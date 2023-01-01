package Rendering;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.stb.STBImage;

public class Texture {
	public int id, height, width;
	public void loadTexture(String path) {
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);

		ByteBuffer data = STBImage.stbi_load(path, width, height, channels, 4);

		if (data == null) System.err.println(STBImage.stbi_failure_reason());

		id = GL13.glGenTextures();
		this.width = width.get();
		this.height = height.get();

		GL13.glBindTexture(GL11.GL_TEXTURE_2D, id);

		GL13.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL13.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL13.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.width, this.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
		STBImage.stbi_image_free(data);
	}

	public void bind() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}

	public void unbind() {
		GL13.glBindTexture(GL13.GL_TEXTURE_2D, 0);
	}

	public void destory() {
		GL13.glDeleteTextures(id);
	}
}