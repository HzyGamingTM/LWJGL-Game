package io;

import Main.Game;
import Rendering.Render;

import static org.lwjgl.glfw.GLFW.*;

public class KeyHandler {
	public static void KeyCallback(long window, int key, int scancode, int action, int mods) {
		if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
			glfwSetWindowShouldClose(window, true);

		if (key == GLFW_KEY_W) {
		}

		if (key == GLFW_KEY_A) {

		}

		if (key == GLFW_KEY_S) {

		}

		if (key == GLFW_KEY_D) {

		}
	}
}