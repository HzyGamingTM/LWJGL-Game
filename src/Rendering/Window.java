package Rendering;

import Main.Game;
import Utils.OurMath;
import io.KeyHandler;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
	public static int width, height;
	private String title;
	private long window;
	public static int fps;
	public static long time;

	public Window(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
	}

	public void create() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
			throw new IllegalStateException("Unable to init GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		window = glfwCreateWindow(width, height, "Gaming", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create window!");

		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			KeyHandler.KeyCallback(window, key, scancode, action, mods);
		});

		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		glfwSetWindowPos(
			window,
			(vidMode.width() - this.width) / 2,
			(vidMode.height() - this.height) / 2
		);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		glfwShowWindow(window);
	}

	public void update() {
		GL.createCapabilities();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		new Game().Init();

		while (!glfwWindowShouldClose(window)) {
			glClearColor(0.25f, 0.7f, 1f, 0.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Render.RenderFrame();
			glfwSwapBuffers(window);
			glfwPollEvents();
			fps++;
			if (System.currentTimeMillis() > time + 1000) {
				OurMath.Vector3 pos = Game.testObject.pos;
				glfwSetWindowTitle(window, title + " | FPS: " + fps + String.format(" Pos: X: %f, Y: %f, Z: %f", pos.x, pos.y, pos.z ));
				time = System.currentTimeMillis();
				fps = 0;
			}
		}
		Destroy();
	}

	void Destroy() {
		Game.shader.destroy();
		Game.testMesh.destroy();

		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
}
