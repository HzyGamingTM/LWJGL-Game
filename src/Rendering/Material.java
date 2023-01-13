package Rendering;

import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.IOException;

public class Material {
	public Texture texture;
	public float height, width;
	public int textureID;

	public Material(String path) {
		try {
			texture = TextureLoader.getTexture(path.split("[.]")[1], Material.class.getResourceAsStream(path), GL30.GL_LINEAR);
		} catch (IOException e) {
			System.err.println("Can't find file at: " + path);
		}
	}

	public void create() {
		width = texture.getWidth();
		height = texture.getHeight();
		textureID = texture.getTextureID();
	}

	public void destroy() {
		GL30.glDeleteTextures(textureID);
	}
}
