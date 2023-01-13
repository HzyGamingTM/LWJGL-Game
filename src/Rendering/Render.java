package Rendering;

import Main.Game;
import Utils.OurMath.Matrix4f;
import org.lwjgl.opengl.GL30;
import java.util.ArrayList;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Render {
	public static double deltaTime = 0f, lastFrame = 0f;
	public static ArrayList<Game.GameObject> renderables = new ArrayList<>();

	public static void RenderFrame() {
		double currentFrame = glfwGetTime();
		deltaTime = currentFrame - lastFrame;
		lastFrame = currentFrame;
		for (int i = 0; i < renderables.size(); i++)
			RenderObject(renderables.get(i), Game.shader);
	}

	public static void RenderObject(Game.GameObject object, Shader shader) {
		GL30.glBindVertexArray(object.mesh.vao);
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, object.mesh.ibo);
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, object.mesh.material.textureID);
		shader.bind();

		Game.Camera camera = Game.mainCamera;
		Matrix4f transformMat, viewMat, proj;
		transformMat = Matrix4f.transform(object.pos, object.rotation, object.scale);
		viewMat = Matrix4f.view(camera.position, camera.rotation);
		proj = Matrix4f.projection(camera.aspect, camera.fov, camera.near, camera.far);

		shader.setUniform("model", transformMat);
		shader.setUniform("projection", proj);
		shader.setUniform("view", viewMat);
		shader.setUniform("aspect", camera.aspect);

		GL30.glDrawElements(GL30.GL_TRIANGLES, object.mesh.indices.length, GL30.GL_UNSIGNED_INT, 0);

		shader.unbind();
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
}