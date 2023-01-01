package Rendering;

import Utils.OurMath;

import org.lwjgl.opengl.GL30;
import Main.Game;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

class console {
	public static void log(String shiddy) {
		System.out.println(shiddy);
	}

	public static void log(OurMath.Matrix4f shiddy) {
		float[] arr = shiddy.getAll();
		for (int i = 0; i < 16; ++i) {
			if (i > 0 && (i & 3) == 0)
				console.log("");
			System.out.print(arr[i] + "  ");
		}
		console.log("");
	}
}

public class Render {
	public static double deltaTime = 0f;
	static double lastFrame = 0f;

	public static void RenderFrame() {
		double currentFrame = glfwGetTime();
		deltaTime = currentFrame - lastFrame;
		lastFrame = currentFrame;
		// Game.testObject.rotation.x += 20 * deltaTime;
		// Game.testObject.rotation.y += 20 * deltaTime;
		// Game.testObject.rotation.z += 20 * deltaTime;
		RenderObject(Game.testObject, Game.shader, (float)lastFrame * 45.f);
	}

	public static void RenderObject(Game.GameObject object, Shader shader, float shiddy) {
		GL30.glBindVertexArray(object.mesh.vao);
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, object.mesh.ibo);
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, object.mesh.material.textureID);
		shader.bind();

		OurMath.Matrix4f transformMat = OurMath.Matrix4f.transform(
			object.pos, object.rotation, object.scale
		);

		OurMath.Matrix4f viewMat = OurMath.Matrix4f.transform(
			Game.mainCamera.position.mul(-1),
			Game.mainCamera.rotation.mul(-1),
			new OurMath.Vector3(1, 1, 1)
		);

		OurMath.Matrix4f projectMat = OurMath.Matrix4f.rotate(
			shiddy, new OurMath.Vector3(0, 1, 0)
		);


		float fovMul = (float)Math.tan(0.5f * Math.toRadians(Game.mainCamera.fov));

		// console.log(OurMath.Matrix4f.multiply(transformMat, Game.mainCamera.projection));

		shader.setUniform("model", OurMath.Matrix4f.identity());
		shader.setUniform("view", projectMat);
		shader.setUniform("fovMul", fovMul);
		shader.setUniform("aspect", Game.mainCamera.aspect);
		// shader.setUniform("projection", projectionMat);

		GL30.glDrawElements(GL30.GL_TRIANGLES, object.mesh.indices.length, GL30.GL_UNSIGNED_INT, 0);
		shader.unbind();
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
}