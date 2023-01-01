package Rendering;

import Utils.FileUtils;
import Utils.OurMath.Vector2;
import Utils.OurMath.Vector3;
import Utils.OurMath.Matrix4f;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class Shader {
	private String vertexFile, fragmentFile;
	private int vertexID, fragmentID, programID;

	public Shader(String vertexPath, String fragmentPath) {
		vertexFile = FileUtils.loadAsString(vertexPath);
		fragmentFile = FileUtils.loadAsString(fragmentPath);
	}

	public void create() {
		programID = GL30.glCreateProgram();
		vertexID = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);

		GL30.glShaderSource(vertexID, vertexFile);
		GL30.glCompileShader(vertexID);

		if (GL30.glGetShaderi(vertexID, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE) {
			System.err.println("Vertex Shader: " + GL30.glGetShaderInfoLog(vertexID));
			return;
		}

		fragmentID = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);

		GL30.glShaderSource(fragmentID, fragmentFile);
		GL30.glCompileShader(fragmentID);

		if (GL30.glGetShaderi(fragmentID, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE) {
			System.err.println("Fragment Shader: " + GL30.glGetShaderInfoLog(fragmentID));
			return;
		}

		GL30.glAttachShader(programID, vertexID);
		GL30.glAttachShader(programID, fragmentID);

		GL30.glLinkProgram(programID);
		if (GL30.glGetProgrami(programID, GL30.GL_LINK_STATUS) == GL30.GL_FALSE) {
			System.err.println("Program Linking: " + GL30.glGetProgramInfoLog(programID));
			return;
		}

		GL30.glValidateProgram(programID);
		if (GL30.glGetProgrami(programID, GL30.GL_VALIDATE_STATUS) == GL30.GL_FALSE) {
			System.err.println("Program Validation: " + GL30.glGetProgramInfoLog(programID));
			return;
		}

		GL30.glDeleteShader(vertexID);
		GL30.glDeleteShader(fragmentID);
	}

	public int getUniformLocation(String name) {
		return GL30.glGetUniformLocation(programID, name);
	}
	public void setUniform(String name, float value) {
		GL30.glUniform1f(getUniformLocation(name), value);
	}
	public void setUniform(String name, int value) {
		GL30.glUniform1i(getUniformLocation(name), value);
	}
	public void setUniform(String name, boolean value) {
		GL30.glUniform1i(getUniformLocation(name), value ? 1 : 0);
	}
	public void setUniform(String name, Vector2 value) {
		GL30.glUniform2f(getUniformLocation(name), value.x, value.y);
	}
	public void setUniform(String name, Vector3 value) {
		GL30.glUniform3f(getUniformLocation(name), value.x, value.y, value.z);
	}
	public void setUniform(String name, Matrix4f value) {
		FloatBuffer matrix = MemoryUtil.memAllocFloat(Matrix4f.SIZE * Matrix4f.SIZE);
		matrix.put(value.getAll()).flip();
		GL30.glUniformMatrix4fv(getUniformLocation(name), true, matrix);
	}

	public void bind() {
		GL30.glUseProgram(programID);
	}
	public void unbind() {
		GL30.glUseProgram(0);
	}
	public void destroy() {
		GL30.glDeleteProgram(programID);
	}
}