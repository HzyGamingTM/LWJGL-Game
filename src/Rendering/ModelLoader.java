package Rendering;

import org.lwjgl.assimp.*;

import Utils.OurMath.Vector3;
import Utils.OurMath.Vector2;

import Rendering.Graphics.Vertex;
import Rendering.Graphics.Mesh;

public class ModelLoader {
	public static Mesh loadModel(String filePath, String texturePath) {
		AIScene scene = Assimp.aiImportFile(filePath, Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate);
		if (scene == null) System.err.println("Couldn't load model at " + filePath);

		AIMesh mesh = AIMesh.create(scene.mMeshes().get(0));
		int vertexCount = mesh.mNumVertices();

		AIVector3D.Buffer vertices = mesh.mVertices();
		AIVector3D.Buffer normals = mesh.mNormals();

		Vertex[] vertexList = new Vertex[vertexCount];

		for (int i = 0; i < vertexCount; i++) {
			AIVector3D vertex = vertices.get(i);
			Vector3 meshVertex = new Vector3(vertex.x(), vertex.y(), vertex.z());

			AIVector3D normal = normals.get(i);
			Vector3 meshNormal = new Vector3(normal.x(), normal.y(), normal.z());

			Vector2 meshTextureCoord = new Vector2(0, 0);
			if (mesh.mNumUVComponents().get(0) != 0) {
				AIVector3D texture = mesh.mTextureCoords(0).get(i);
				meshTextureCoord.x = texture.x();
				meshTextureCoord.y = texture.y();
			}

			vertexList[i] = new Graphics.Vertex(meshVertex, meshNormal, meshTextureCoord);
		}

		int faceCount = mesh.mNumFaces();
		AIFace.Buffer indices = mesh.mFaces();
		int[] indicesList = new int[faceCount * 3];

		for (int i = 0; i < faceCount; i++) {
			AIFace face = indices.get(i);
			indicesList[i * 3 + 0] = face.mIndices().get(0);
			indicesList[i * 3 + 1] = face.mIndices().get(1);
			indicesList[i * 3 + 2] = face.mIndices().get(2);
		}

		return new Mesh(vertexList, indicesList, new Material(texturePath));
	}
}
