package Rendering;

import Main.Game;
import Utils.OurMath;
import io.KeyHandler;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
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

	GLFWWindowSizeCallback sizeCallback;
	boolean isResized;


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

		window = glfwCreateWindow(width, height, "Gaming", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create window!");
		createCallbacks();

		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(
			window,
			(vidMode.width() - this.width) / 2,
			(vidMode.height() - this.height) / 2
		);
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);

		GL.createCapabilities();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		new Game().Init();
	}

	public void createCallbacks() {
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			KeyHandler.KeyCallback(window, key, scancode, action, mods);
		});
		sizeCallback = new GLFWWindowSizeCallback() {
			public void invoke(long win, int w, int h) {
				width = w;
				height = h;
				isResized = true;
			}
		};
	}

	public void update() {
		while (!glfwWindowShouldClose(window)) {
			if (isResized) {
				GL11.glViewport(0, 0, width, height);
				isResized = false;
			}
			glClearColor(0.25f, 0.7f, 1f, 0.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Render.RenderFrame();
			glfwSwapBuffers(window);
			glfwPollEvents();
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
