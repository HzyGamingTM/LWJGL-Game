package io;

import Main.Game;
import Rendering.Render;

import static org.lwjgl.glfw.GLFW.*;

public class KeyHandler {
	public static void KeyCallback(long window, int key, int scancode, int action, int mods) {
		if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
			glfwSetWindowShouldClose(window, true);

		if (key == GLFW_KEY_W) {
			Game.mainCamera.position.z -= 3 * Render.deltaTime;
		}

		if (key == GLFW_KEY_A) {
			Game.mainCamera.position.x -= 3 * Render.deltaTime;
		}

		if (key == GLFW_KEY_S) {
			Game.mainCamera.position.z += 3 * Render.deltaTime;
		}

		if (key == GLFW_KEY_D) {
			Game.mainCamera.position.x += 3 * Render.deltaTime;
		}
	}
}