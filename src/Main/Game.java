package Main;

import Rendering.Graphics.Mesh;
import Rendering.Material;
import Rendering.Shader;
import Rendering.Graphics.Vertex;

import Utils.OurMath.Vector3;
import Utils.OurMath.Vector2;


public class Game {
	public static Camera mainCamera;
	public static Shader shader;
	public static Mesh testMesh;
	public static Material testMat;
	public static GameObject testObject;


	public void Init() {
		mainCamera = new Camera(
			89f, 0.01f, 1000f, 0.75f,
			new Vector3(0, 0, 1), new Vector3(0, 0, 0)
		);

		testMat = new Material("/textures/image.png");
		testMesh = new Mesh(new Vertex[] {
			new Vertex(new Vector3(-0.5f,  0.5f, 0.0f), new Vector3(1.0f, 0.0f, 0.0f), new Vector2(0.0f, 0.0f)),
			new Vertex(new Vector3(-0.5f, -0.5f, 0.0f), new Vector3(0.0f, 1.0f, 0.0f), new Vector2(0.0f, 1.0f)),
			new Vertex(new Vector3( 0.5f, -0.5f, 0.0f), new Vector3(0.0f, 0.0f, 1.0f), new Vector2(1.0f, 1.0f)),
			new Vertex(new Vector3( 0.5f,  0.5f, 0.0f), new Vector3(1.0f, 1.0f, 0.0f), new Vector2(1.0f, 0.0f))
		}, new int[] {
			0, 1, 2,
			0, 3, 2
		}, testMat);
		testMesh.create();

		testObject = new GameObject(
			new Vector3(0f, 0f, -1f),
			new Vector3(0f, 0f, 0f),
			new Vector3(1f, 1f, 1f),
			testMesh
		);

		shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl"); // NOTE: This has to be a Class Path!
		shader.create();
	}

	public class GameObject {
		public Vector3 pos, scale, rotation;
		public Mesh mesh;

		public GameObject(Vector3 pos, Vector3 rotation, Vector3 scale, Mesh mesh) {
			this.pos = pos;
			this.rotation = rotation;
			this.scale = scale;
			this.mesh = mesh;
		}
	}

	public class Camera {
		public float fov, near, far, aspect;
		public Vector3 position, rotation;

		public Camera(float fov, float near, float far, float aspect, Vector3 position, Vector3 rotation) {
			this.fov = fov;
			this.near = near;
			this.far = far;
			this.aspect = aspect;
			this.position = position;
			this.rotation = rotation;
		}
	}
}