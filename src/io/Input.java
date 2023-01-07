package io;

import org.lwjgl.glfw.*;

public class Input {
	public static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
	public static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
	public static double mouseX, mouseY;
	public static double scrollX, scrollY;

	public GLFWKeyCallback keyboard;
	public GLFWCursorPosCallback mouseMove;
	public GLFWMouseButtonCallback mouseButtons;
	public GLFWScrollCallback mouseScroll;

	public Input() {
		keyboard = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				keys[key] = (action != GLFW.GLFW_RELEASE);
			}
		};

		mouseMove = new GLFWCursorPosCallback() {
			public void invoke(long window, double xpos, double ypos) {
				mouseX = xpos;
				mouseY = ypos;
			}
		};

		mouseButtons = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				buttons[button] = (action != GLFW.GLFW_RELEASE);
			}
		};

		mouseScroll = new GLFWScrollCallback() {
			public void invoke(long window, double offsetx, double offsety) {
				scrollX += offsetx;
				scrollY += offsety;
			}
		};
	}

	public static boolean isKeyDown(int key) {
		return keys[key];
	}

	public static boolean isButtonDown(int button) {
		return buttons[button];
	}

	public void destroy() {
		keyboard.free();
		mouseMove.free();
		mouseButtons.free();
		mouseScroll.free();
	}
}