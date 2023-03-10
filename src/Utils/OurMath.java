package Utils;

public class OurMath {
	public static float Clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}
	public static float Lerp(float a, float b, float f) {
		return a + f * (b - a);
	}
	public static float PreciseLerp(float a, float b, float f) {
		return a + f * (b - a);
	}
	public static class Vector3 {
		public float x, y, z;
		public Vector3(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		public Vector3 add(Vector3 o) {
			return new Vector3(this.x + o.x, this.y + o.y, this.z + o.z);
		}

		public Vector3 sub(Vector3 o) {
			return new Vector3(this.x + o.x, this.y + o.y, this.z + o.z);
		}

		public Vector3 mul(Vector3 o) {
			return new Vector3(this.x * o.x, this.y * o.y, this.z * o.z);
		}

		public Vector3 div(Vector3 o) {
			return new Vector3(this.x / o.x, this.y / o.y, this.z / o.z);
		}

		public Vector3 mul(float o) {
			return new Vector3(this.x * o, this.y * o, this.z * o);
		}

		public Vector3 div(float o) {
			return new Vector3(this.x / o, this.y / o, this.z / o);
		}
		public Vector3 zero() { return new Vector3(0, 0, 0); }
		public float length() {
			return (float) Math.sqrt(
				this.x * this.x + this.y * this.y + this.z * this.z
			);
		}
		public float dot(Vector3 o) {
			return this.x * o.x + this.y * o.y + this.z * o.z;
		}
		public Vector3 cross(Vector3 o) {
			return new Vector3(
				this.y * o.z + this.z * o.y,
				this.z * o.x + this.x * o.z,
				this.x * o.y + this.y * o.x
			);
		}
	}
	public static class Vector2 {
		public float x, y;
		public Vector2(float x, float y) {
			this.x = x;
			this.y = y;
		}
		public Vector2 add(Vector2 o) {
			return new Vector2(this.x + o.x, this.y + o.y);
		}
		public Vector2 sub(Vector2 o) {
			return new Vector2(this.x + o.x, this.y + o.y);
		}
		public Vector2 mul(Vector2 o) {
			return new Vector2(this.x * o.x, this.y * o.y);
		}
		public Vector2 div(Vector2 o) {
			return new Vector2(this.x / o.x, this.y / o.y);
		}
		public Vector2 zero() { return new Vector2(0, 0); }
		public float length() {
			return (float) Math.sqrt(
				this.x * this.x + this.y * this.y
			);
		}
		public float dot(Vector2 o) {
			return this.x * o.x + this.y * o.y;
		}
		public float cross(Vector2 o) {
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
		public static Matrix4f translate(Vector3 translate) {
			Matrix4f result = Matrix4f.identity();
			result.set(3, 0, translate.x);
			result.set(3, 1, translate.y);
			result.set(3, 2, translate.z);
			return result;
		}
		public static Matrix4f rotate(float angle, Vector3 axis) {
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
		public static Matrix4f scale(Vector3 vec) {
			Matrix4f result = Matrix4f.identity();
			result.set(0, 0, vec.x);
			result.set(1, 1, vec.y);
			result.set(2, 2, vec.z);
			return result;
		}
		public static Matrix4f scale(float scalar) {
			Matrix4f result = Matrix4f.identity();
			result.set(0, 0, scalar);
			result.set(1, 1, scalar);
			result.set(2, 2, scalar);
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
		public static Matrix4f transform(Vector3 pos, Vector3 rot, Vector3 scale) {
			Matrix4f translationMatrix = Matrix4f.translate(pos);
			Matrix4f rotXMatrix = Matrix4f.rotate(rot.x, new Vector3(1, 0, 0));
			Matrix4f rotYMatrix = Matrix4f.rotate(rot.y, new Vector3(0, 1, 0));
			Matrix4f rotZMatrix = Matrix4f.rotate(rot.z, new Vector3(0, 0, 1));
			Matrix4f scaleMatrix = Matrix4f.scale(scale);
			Matrix4f rotationMatrix = Matrix4f.multiply(rotXMatrix, Matrix4f.multiply(rotYMatrix, rotZMatrix));
			Matrix4f result = Matrix4f.multiply(translationMatrix, Matrix4f.multiply(rotationMatrix, scaleMatrix));
			return result;
		}
		public static Matrix4f view(Vector3 pos, Vector3 rot) {
			Matrix4f translationMatrix = Matrix4f.translate(pos.mul(-1));
			Matrix4f rotXMatrix = Matrix4f.rotate(rot.x, new Vector3(1, 0, 0));
			Matrix4f rotYMatrix = Matrix4f.rotate(rot.y, new Vector3(0, 1, 0));
			Matrix4f rotZMatrix = Matrix4f.rotate(rot.z, new Vector3(0, 0, 1));
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