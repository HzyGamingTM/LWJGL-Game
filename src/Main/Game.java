package Main;

import Rendering.*;
import Rendering.Graphics.Mesh;

import Utils.OurMath;
import Utils.OurMath.Vector2f;
import Utils.OurMath.Vector3f;

import io.Input;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;


public class Game {
	public static Camera mainCamera;
	public static Shader shader;
	public static Shader crosshairShader;
	public static GameObject testObject, floorObject;
	public static float mouseSens = 0.4f, speed = 1f;
	static double oldMouseX, oldMouseY, newMouseX, newMouseY;

	public void Init() {
		mainCamera = new Camera(
			89f, 0.01f, 1000f, (float)Window.width / Window.height,
			new Vector3f(0, 0, 1), new Vector3f(0, 0, 0)
		);

		Mesh testMesh = ModelLoader.loadModel("resources/models/pawn.obj", "/textures/image.png");
		testMesh.create();

		testObject = new GameObject(
			new Vector3f(0f, 0f, -1f),
			new Vector3f(0f, 0f, 0f),
			new Vector3f(1f, 1f, 1f),
			testMesh
		);

		Mesh floorMesh = ModelLoader.loadModel("resources/models/floor.obj", "/textures/grass.jpg");
		floorMesh.create();
		floorObject = new GameObject(
			new Vector3f(0f, -5f, -1f),
			new Vector3f(0f, 0f, 0f),
			new Vector3f(1f, 1f, 1f),
			floorMesh
		);

		shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");
		shader.create();

		crosshairShader = new Shader("/shaders/crosshairShaderVertex.glsl","/shaders/crosshairShaderFrag.glsl");
		crosshairShader.create();

		Graphics.GUI testGui = new Graphics.GUI(
			new Vector2f[]{
				new Vector2f(0, 0), new Vector2f(1, 0),
				new Vector2f(0, -1), new Vector2f(1, -1)
			}, new Material("/textures/black_rover.png")
		);

		Render.guis.add(testGui);
		Render.renderables.add(testObject);
		Render.renderables.add(floorObject);
	}

	public static void Update() {
		mainCamera.aspect = (float) Window.width / Window.height;
		newMouseX = Input.mouseX;
		newMouseY = Input.mouseY;

		float rotationY, x, z;
		rotationY = mainCamera.rotation.y;
		x = (float)(Math.sin(Math.toRadians(rotationY)) * speed * Render.deltaTime);
		z = (float)(Math.cos(Math.toRadians(rotationY)) * speed * Render.deltaTime);

		if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			speed = 2;
		} else {
			speed = 1;
		}

		if (Input.isKeyDown(GLFW.GLFW_KEY_W)) {
			mainCamera.position.z -= z;
			mainCamera.position.x -= x;
		}

		if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
			mainCamera.position.z += x;
			mainCamera.position.x -= z;
		}

		if (Input.isKeyDown(GLFW.GLFW_KEY_S)) {
			mainCamera.position.x += x;
			mainCamera.position.z += z;
		}

		if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
			mainCamera.position.z -= x;
			mainCamera.position.x += z;
		}

		if (Input.isKeyDown(GLFW_KEY_SPACE))
			mainCamera.position.y += speed * Render.deltaTime;

		if (Input.isKeyDown(GLFW_KEY_LEFT_CONTROL))
			mainCamera.position.y -= speed * Render.deltaTime;


		if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE))
			glfwSetInputMode(Window.window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);

		if (Input.isKeyDown(GLFW.GLFW_KEY_F11))
			Window.setFullscreen();

		float dx = (float) (newMouseX - oldMouseX);
		float dy = (float) (newMouseY - oldMouseY);

		mainCamera.rotation.x += -dy * mouseSens;
		mainCamera.rotation.x = Math.max(-89.9f, Math.min(89.9f, mainCamera.rotation.x));
		mainCamera.rotation.y += -dx * mouseSens;

		oldMouseX = newMouseX;
		oldMouseY = newMouseY;

		if (OurMath.RayTestObject(new OurMath.Line3f(mainCamera.position, OurMath.eulerToDir(mainCamera.rotation)), floorObject)) {
			System.out.print(BRUH++);
			System.out.printf(" [%.3f %.3f %.3f]\n", mainCamera.rotation.x, mainCamera.rotation.y, mainCamera.rotation.z);
		}
		/*
		if (new OurMath.Triangle(
			new Vector3f(1.279f, 2.913f, -6.783f),
			new Vector3f(1.38f, 2.913f, -6.752f),
			new Vector3f(1.349f, 3.011f, -6.677f)
			).containsPoint(new Vector3f(-0.003f, -0.017f, 1.000f))) {
			System.out.println("bruh");
		}
		 */
	}

	public static void Destroy() {
		for (int i = 0; i < Render.renderables.size(); i++)
			Render.renderables.get(i).mesh.destroy();
		for (int i = 0; i < Render.guis.size(); i++)
			Render.guis.get(i).destroy();

		Game.shader.destroy();
	}

	public class GameObject {
		public Vector3f pos, scale, rotation, velocity;
		public Mesh mesh;

		public GameObject(Vector3f pos, Vector3f rotation, Vector3f scale, Mesh mesh) {
			this.pos = pos;
			this.rotation = rotation;
			this.scale = scale;
			this.mesh = mesh;
		}
	}

	public class Camera {
		public float fov, near, far, aspect;
		public Vector3f position, rotation;

		public Camera(float fov, float near, float far, float aspect, Vector3f position, Vector3f rotation) {
			this.fov = fov;
			this.near = near;
			this.far = far;
			this.aspect = aspect;
			this.position = position;
			this.rotation = rotation;
		}
	}
}