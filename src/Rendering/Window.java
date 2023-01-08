package Rendering;

import Main.Game;
import Utils.OurMath;

import io.Input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjglx.opengl.Display;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.opengl.GL11.*;

public class Window {
	public static int width, height;
	public Input input;
	private String title;
	public static long window;
	public static int fps;
	public static long time;
	public static boolean isFullscreen;
	public static boolean isResized;
	private GLFWWindowSizeCallback sizeCallback;
	private static int[] windowPosX = new int[1], windowPosY = new int[1];

	public Window(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
	}

	public void create() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) throw new IllegalStateException("Unable to init GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		window = glfwCreateWindow(width, height, title, isFullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);
		if (window == 0) throw new RuntimeException("Failed to create window!");

		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		windowPosX[0] = (vidMode.width() - width) / 2;
		windowPosY[0] = (vidMode.height() - height) / 2;

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);

		GL.createCapabilities();
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		new Game().Init();
		input = new Input();

		createCallbacks();
	}

	public void createCallbacks() {
		sizeCallback = new GLFWWindowSizeCallback() {
			public void invoke(long win, int w, int h) {
				width = w;
				height = h;
				isResized = true;
			}
		};

		glfwSetKeyCallback(window, input.keyboard);
		glfwSetCursorPosCallback(window, input.mouseMove);
		glfwSetMouseButtonCallback(window, input.mouseButtons);
		glfwSetScrollCallback(window, input.mouseScroll);
		glfwSetWindowSizeCallback(window, sizeCallback);
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	}

	public void update() {
		while (!glfwWindowShouldClose(window)) {
			if (isResized) {
				GL11.glViewport(0, 0, width, height);
				isResized = false;
			}

			glfwPollEvents();
			glClearColor(0.25f, 0.7f, 1f, 0.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Render.RenderFrame();
			glfwSwapBuffers(window);
			Game.Update();

			fps++;
			if (System.currentTimeMillis() > time + 1000) {
				OurMath.Vector3 pos = Game.testObject.pos;
				OurMath.Vector3 camPos = Game.mainCamera.position;
				glfwSetWindowTitle(window, title +
					String.format(" | FPS: %s OBJ Pos: X: %f Y: %f Z: %f Camera: X: %f Y: %f Z: %f", fps, pos.x, pos.y, pos.z, camPos.x, camPos.y, camPos.z)
				);
				time = System.currentTimeMillis();
				fps = 0;
			}
		}
		destroy();
	}

	public static void setFullscreen() {
		Window.isFullscreen = !isFullscreen;
		if (isFullscreen) {
			glfwGetWindowPos(window, windowPosX, windowPosY);
			glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 144);
		} else {
			GLFW.glfwSetWindowMonitor(window, 0, windowPosX[0], windowPosY[0], width, height, 144);
		}
		GL11.glViewport(0, 0, width, height);
	}

	void destroy() {
		new Input().destroy();
		Game.shader.destroy();
		Game.testMesh.destroy();
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
}