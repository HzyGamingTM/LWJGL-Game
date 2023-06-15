package Utils;

import Main.Game;
import Rendering.Graphics.Vertex;

public class OurMath {
	public static float Clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}
	public static float Lerp(float a, float b, float f) {
		return a + f * (b - a);
	}
	public static float PreciseLerp(float a, float b, float f) {
		return a * (1f - f) + (b * f);
	}
	public static class Vector3f {
		public float x, y, z;
		public static final Vector3f ZERO = new Vector3f(0, 0, 0);
		public Vector3f(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		public Vector3f add(Vector3f o) {
			return new Vector3f(this.x + o.x, this.y + o.y, this.z + o.z);
		}
		public Vector3f sub(Vector3f o) {
			return new Vector3f(this.x - o.x, this.y - o.y, this.z - o.z);
		}
		public Vector3f mul(Vector3f o) {
			return new Vector3f(this.x * o.x, this.y * o.y, this.z * o.z);
		}
		public Vector3f div(Vector3f o) {
			return new Vector3f(this.x / o.x, this.y / o.y, this.z / o.z);
		}
		public Vector3f mul(float o) {
			return new Vector3f(this.x * o, this.y * o, this.z * o);
		}
		public Vector3f div(float o) {
			return new Vector3f(this.x / o, this.y / o, this.z / o);
		}
		public Vector3f normed() {
			float l = 1.f / this.length();
			return new Vector3f(this.x * l, this.y * l, this.z * l);
		}
		public float length() {
			return (float) Math.sqrt(
				this.x * this.x + this.y * this.y + this.z * this.z
			);
		}
		public float dot(Vector3f o) {
			return this.x * o.x + this.y * o.y + this.z * o.z;
		}
		public static float dot(Vector3f a, Vector3f b) {
			return a.x * b.x + a.y * b.y + a.z * b.z;
		}
		public Vector3f cross(Vector3f o) {
			return new Vector3f(
				this.y * o.z - this.z * o.y,
				this.z * o.x - this.x * o.z,
				this.x * o.y - this.y * o.x
			);
		}
	}
	public static Vector3f eulerToDir(Vector3f euler) {
		float x = (float) (Math.cos(Math.toRadians(euler.y)) * Math.cos(Math.toRadians(euler.x)));
		float z = (float) (Math.sin(Math.toRadians(euler.y)) * Math.cos(Math.toRadians(euler.x)));
		float y = (float) (Math.sin(Math.toRadians(euler.x)));
		return new Vector3f(x, y, z);
	}

	public static Vector3f RayCast(Line3f line, Vector3f pp, Vector3f np) {
		return line.pos.add(line.dir.mul(pp.sub(line.pos).dot(np) / line.dir.dot(np)));
	}

	public static float RayCastD(Line3f line, Vector3f pp, Vector3f np) {
		return pp.sub(line.pos).dot(np) / line.dir.dot(np);
	}



	public static Vector3f RayCastObject(Line3f line, Game.GameObject object) {
		// Adds and create triangle object from mesh
		Vector3f winnerVector = null;
		float winnerDistance = Game.mainCamera.far;
		
		for (int i = 0; i < object.mesh.indices.length; i += 3) {
			Triangle triangle = new Triangle(
				object.mesh.vertices[object.mesh.indices[i]],
				object.mesh.vertices[object.mesh.indices[i + 1]],
				object.mesh.vertices[object.mesh.indices[i + 2]]
			);

			triangle.a = triangle.a.add(object.pos);
			triangle.b = triangle.b.add(object.pos);
			triangle.c = triangle.c.add(object.pos);

			// Step 1: finding P
			Vector3f normal = triangle.getNormal();
			Vector3f dir = line.dir.normed();

			if (Math.abs(normal.dot(dir)) < 0.001f) { // Epsilon = Threshold
				continue;
			}

			float t = RayCastD(line, triangle.a, normal);
			// check if the triangle is behind the ray
			if (t < Game.mainCamera.near) continue; // the triangle is behind

			Vector3f P = line.pos.add(dir.mul(t));

			if (triangle.containsPoint(P))
				if (t < winnerDistance) {
					winnerDistance = t;
					winnerVector = P;
				}
		}

		return winnerVector;
	}

	public static Vector3f[] arr = new Vector3f[3];
	public static boolean arrHit = false;

	public static boolean RayTestObject(Line3f line, Game.GameObject object) {
		// Adds and create triangle object from mesh

		for (int i = 0; i < object.mesh.indices.length; i += 3) {
			Triangle triangle = new Triangle(
				object.mesh.vertices[object.mesh.indices[i]],
				object.mesh.vertices[object.mesh.indices[i + 1]],
				object.mesh.vertices[object.mesh.indices[i + 2]]
			);

			triangle.a = triangle.a.add(object.pos);
			triangle.b = triangle.b.add(object.pos);
			triangle.c = triangle.c.add(object.pos);

			// Step 1: finding P
			Vector3f normal = triangle.getNormal();
			Vector3f dir = line.dir.normed();

			if (Math.abs(normal.dot(dir)) < 0.001f) { // Epsilon = Threshold
				continue;
			}

			float t = RayCastD(line, triangle.a, normal);
			// check if the triangle is behind the ray
			if (t < Game.mainCamera.near) continue; // the triangle is behind

			Vector3f P = line.pos.add(dir.mul(t));
			if (triangle.containsPoint(P)) {
				System.out.printf(
					"Hit triangle with vertices: [(%.3f %.3f %.3f), (%.3f %.3f %.3f), (%.3f %.3f %.3f)]\nat point (%.3f %.3f %.3f)\nfrom [(%.3f %.3f %.3f), (%.3f, %.3f, %.3f)]%n", triangle.a.x, triangle.a.y, triangle.a.z,
					triangle.b.x, triangle.b.y, triangle.b.z,
					triangle.c.x, triangle.c.y, triangle.c.z,
					P.x, P.y, P.z,
					line.pos.x, line.pos.y, line.pos.z,
					line.dir.x, line.dir.y, line.dir.z
				);
				arr[0] = triangle.a;
				arr[1] = triangle.b;
				arr[2] = triangle.c;
				arrHit = true;
				return true;
			}
		}

		return false;
	}

	public static class Line3f {
		public Vector3f pos, dir;
		public Line3f(Vector3f pos, Vector3f dir) {
			this.pos = pos;
			this.dir = dir;
		}
	}

	public static class Triangle {
		Vector3f a, b, c;

		public Triangle(Vertex a, Vertex b, Vertex c) {
			this.a = a.pos;
			this.b = b.pos;
			this.c = c.pos;
		}

		public Triangle(Vector3f a, Vector3f b, Vector3f c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}

		public Vector3f getNormalUN() {
			return a.sub(b).cross(c.sub(b));
		}
		public Vector3f getNormal() {
			return a.sub(b).cross(c.sub(b)).normed();
		}

		//	Vec3f v0v1 = v1 - v0;
		//  Vec3f v0v2 = v2 - v0;
		//  Vec3f N = v0v1.crossProduct(v0v2); // N
		//  float area2 = N.length();

		// https://www.scratchapixel.com/images/ray-triangle/triinsideout3.png?
		public boolean containsPoint(Vector3f p) {
			Vector3f normal = this.getNormal();

			boolean sign = (a.sub(b).cross(b.sub(p)).dot(normal) >= 0);
			if ((b.sub(c).cross(c.sub(p)).dot(normal) >= 0) != sign) return false;
			if ((c.sub(a).cross(a.sub(p)).dot(normal) >= 0) == sign) {
				System.out.printf("Normal: %f %f %f\n", normal.x, normal.y, normal.z);
				return true;
			}
			return false;
		}
	}

	/*
	vec3 ilp3(vec3 pl, vec3 _dl, vec3 pp, vec3 np) {
		vec3 dl = vnorm3(_dl);
		float d = vdot3(vsub3(pp, pl), np) / vdot3(dl, np);
		vec3 p = vadd3(pl, vmul3(dl, { d, d, d }));
		return p;
	}
	 */

	public static class Vector2f {
		public float x, y;
		public Vector2f(float x, float y) {
			this.x = x;
			this.y = y;
		}
		public Vector2f add(Vector2f o) {
			return new Vector2f(this.x + o.x, this.y + o.y);
		}
		public Vector2f sub(Vector2f o) {
			return new Vector2f(this.x - o.x, this.y - o.y);
		}
		public Vector2f mul(Vector2f o) {
			return new Vector2f(this.x * o.x, this.y * o.y);
		}
		public Vector2f div(Vector2f o) {
			return new Vector2f(this.x / o.x, this.y / o.y);
		}
		public Vector2f zero() { return new Vector2f(0, 0); }
		public float length() {
			return (float) Math.sqrt(
				this.x * this.x + this.y * this.y
			);
		}
		public float dot(Vector2f o) {
			return this.x * o.x + this.y * o.y;
		}
		public float cross(Vector2f o) {
			return this.x * o.y + this.y * o.x;
		}
	}
	public static class Matrix4f {
		public static final int SIZE = 4;
		public float[] elements = new float[SIZE * SIZE];
		public float get(int x, int y) {
			return elements[y * SIZE + x];
		}
		public void set(int x, int y, float value) {
			elements[y * SIZE + x] = value;
		}
		public float[] getAll() { return elements; }
		public static Matrix4f identity() {
			Matrix4f result = new Matrix4f();

			for (int i = 0; i < SIZE; i++)
				for (int j = 0; j < SIZE; j++)
					result.set(i, j, 0);

			result.set(0, 0, 1);
			result.set(1, 1, 1);
			result.set(2, 2, 1);
			result.set(3, 3, 1);

			return result;
		}
		public static Matrix4f translate(Vector3f translate) {
			Matrix4f result = Matrix4f.identity();
			result.set(3, 0, translate.x);
			result.set(3, 1, translate.y);
			result.set(3, 2, translate.z);
			return result;
		}
		public static Matrix4f rotate(float angle, Vector3f axis) {
			Matrix4f result = Matrix4f.identity();

			float cos = (float) Math.cos(Math.toRadians(angle));
			float sin = (float) Math.sin(Math.toRadians(angle));
			float C = 1 - cos;

			result.set(0, 0, cos + axis.x * axis.x * C);
			result.set(0, 1, axis.x * axis.y * C - axis.z * sin);
			result.set(0, 2, axis.x * axis.z * C + axis.y * sin);
			result.set(1, 0, axis.y * axis.x * C + axis.z * sin);
			result.set(1, 1, cos + axis.y * axis.y * C);
			result.set(1, 2, axis.y * axis.z * C - axis.x * sin);
			result.set(2, 0, axis.z * axis.x * C - axis.y * sin);
			result.set(2, 1, axis.z * axis.y * C + axis.x * sin);
			result.set(2, 2, cos + axis.z * axis.z * C);

			return result;
		}
		public static Matrix4f scale(Vector3f vec) {
			Matrix4f result = Matrix4f.identity();
			result.set(0, 0, vec.x);
			result.set(1, 1, vec.y);
			result.set(2, 2, vec.z);
			return result;
		}
		public static Matrix4f projection(float aspect, float fov, float near, float far) {
			Matrix4f result = Matrix4f.identity();
			float tanFOV = (float)Math.tan(Math.toRadians(fov / 2));
			float range = far - near;
			float zp = far + near;
			result.set(0, 0, 1f / aspect * tanFOV);
			result.set(1, 1, 1f / tanFOV);
			result.set(2, 2, -zp / range);
			result.set(2, 3, -1f);
			result.set(3, 2, -((2 * far * near) / range));
			result.set(3, 3, 0f);
			return result;
		}
		public static Matrix4f transform(Vector3f pos, Vector3f rot, Vector3f scale) {
			Matrix4f translationMatrix = Matrix4f.translate(pos);
			Matrix4f rotXMatrix = Matrix4f.rotate(rot.x, new Vector3f(1, 0, 0));
			Matrix4f rotYMatrix = Matrix4f.rotate(rot.y, new Vector3f(0, 1, 0));
			Matrix4f rotZMatrix = Matrix4f.rotate(rot.z, new Vector3f(0, 0, 1));
			Matrix4f scaleMatrix = Matrix4f.scale(scale);
			Matrix4f rotationMatrix = Matrix4f.multiply(rotXMatrix, Matrix4f.multiply(rotYMatrix, rotZMatrix));
			Matrix4f result = Matrix4f.multiply(translationMatrix, Matrix4f.multiply(rotationMatrix, scaleMatrix));
			return result;
		}
		public static Matrix4f view(Vector3f pos, Vector3f rot) {
			Matrix4f translationMatrix = Matrix4f.translate(pos.mul(-1));
			Matrix4f rotXMatrix = Matrix4f.rotate(rot.x, new Vector3f(1, 0, 0));
			Matrix4f rotYMatrix = Matrix4f.rotate(rot.y, new Vector3f(0, 1, 0));
			Matrix4f rotZMatrix = Matrix4f.rotate(rot.z, new Vector3f(0, 0, 1));
			Matrix4f rotationMatrix = Matrix4f.multiply(rotZMatrix, Matrix4f.multiply(rotYMatrix, rotXMatrix));
			return Matrix4f.multiply(translationMatrix, rotationMatrix);
		}
		public static Matrix4f multiply(Matrix4f matrix, Matrix4f other) {
			Matrix4f result = Matrix4f.identity();
			for (int i = 0; i < SIZE; i++)
				for (int j = 0; j < SIZE; j++)
					result.set(i, j,
						matrix.get(i, 0) * other.get(0, j) +
						matrix.get(i, 1) * other.get(1, j) +
						matrix.get(i, 2) * other.get(2, j) +
						matrix.get(i, 3) * other.get(3, j)
					);
			return result;
		}
	}
}