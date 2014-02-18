package ca.wacos.opengltest;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;

import java.io.FileInputStream;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

public class ShaderManager {

	public static final String POST_VERTEX_SHADER = "ca/wacos/opengltest/shaders/fxaa.vert";
	public static final String POST_FRAGMENT_SHADER = "ca/wacos/opengltest/shaders/fxaa.frag";

	public static final String LIGHTING_VERTEX_SHADER = "ca/wacos/opengltest/shaders/lighting.vert";
	public static final String LIGHTING_FRAGMENT_SHADER = "ca/wacos/opengltest/shaders/lighting.frag";

	public static final String AMBIENT_OCCLUSION_VERTEX_SHADER = "ca/wacos/opengltest/shaders/ambient_occlusion.vert";
	public static final String AMBIENT_OCCLUSION_FRAGMENT_SHADER = "ca/wacos/opengltest/shaders/ambient_occlusion.frag";

	private static int postProgram;
	private static int lightingProgram;
	private static int occlusionProgram;
	private static int framebufferID;
	private static int colorTextureID;
	private static int depthRenderBufferID;

	public static FrameBuffer aoBuffer = new FrameBuffer();
	public static FrameBuffer initialBuffer = new FrameBuffer();

	public static void load() {
		postProgram = createShader(POST_FRAGMENT_SHADER, POST_VERTEX_SHADER);
		// lightingProgram = createShader(LIGHTING_FRAGMENT_SHADER, LIGHTING_VERTEX_SHADER);
		occlusionProgram = createShader(AMBIENT_OCCLUSION_FRAGMENT_SHADER, AMBIENT_OCCLUSION_VERTEX_SHADER);

		initialBuffer.setupFBO();
		aoBuffer.setupFBO();
	}
	public static int createShader(String frag, String vert) {

		String fragFile = getData(frag);
		String vertexFile = getData(vert);

		int compiledFrag = compile(fragFile, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		int compiledVertex = compile(vertexFile, ARBVertexShader.GL_VERTEX_SHADER_ARB);

		int program = ARBShaderObjects.glCreateProgramObjectARB();

		ARBShaderObjects.glAttachObjectARB(program, compiledVertex);
		ARBShaderObjects.glAttachObjectARB(program, compiledFrag);

		ARBShaderObjects.glLinkProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL_FALSE) {
			System.out.println(log(program));
		}

		ARBShaderObjects.glValidateProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL_FALSE) {
			System.out.println(log(program));
		}

		return program;
	}
	public static void enableAmbientOcclusion() {

		ARBShaderObjects.glUseProgramObjectARB(occlusionProgram);

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, initialBuffer.getColorTextureID());

		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, initialBuffer.getDepthTextureID());

		int sampler0 = glGetUniformLocation(occlusionProgram, "tex0");
		glUniform1i(sampler0, 0);

		int sampler1 = glGetUniformLocation(occlusionProgram, "tex1");
		glUniform1i(sampler1, 1);

		int zNear = glGetUniformLocation(occlusionProgram, "zNear");
		glUniform1f(zNear, Vars.ZNEAR);
		int zFar = glGetUniformLocation(occlusionProgram, "zFar");
		glUniform1f(zFar, Vars.ZFAR);
	}
	public static void enableLighting(Matrix4f cameraMatrix) {

		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);

		ARBShaderObjects.glUseProgramObjectARB(lightingProgram);

		int cameraPosition = glGetUniformLocation(postProgram, "cameraPosition");
		int camera = glGetUniformLocation(postProgram, "camera");
		int model = glGetUniformLocation(postProgram, "model");

		cameraMatrix.store(buffer);
		buffer.flip();
		glUniformMatrix4(camera, false, buffer);
		modelMatrix().store(buffer);
		buffer.flip();
		glUniformMatrix4(model, false, buffer);
		glUniform3f(cameraPosition, cameraMatrix.m30, cameraMatrix.m31, cameraMatrix.m32);
	}
	private static Matrix4f modelMatrix() {
		Matrix4f m = new Matrix4f();
		m.m33 = 1;
		return m;
	}
	public static void enableFXAA() {

		ARBShaderObjects.glUseProgramObjectARB(postProgram);

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, aoBuffer.getColorTextureID());

		int w = glGetUniformLocation(postProgram, "rt_w");
		int h = glGetUniformLocation(postProgram, "rt_h");
		int sampler = glGetUniformLocation(postProgram, "tex0");
		glUniform1f(w, Display.getWidth());
		glUniform1f(h, Display.getHeight());
		glUniform1i(sampler, 0);
	}
	public static void disableShader() {
		ARBShaderObjects.glUseProgramObjectARB(0);
	}
	private static int compile(String data, int type) {
		int shader = 0;
		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB(type);

			if (shader == 0) return 0;

			ARBShaderObjects.glShaderSourceARB(shader, data);
			ARBShaderObjects.glCompileShaderARB(shader);

			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE) {
				System.out.println(log(shader));
				ARBShaderObjects.glDeleteObjectARB(shader);
				return 0;
			}
		}
		catch (Exception e) {
			System.out.println("Error while compiling fragment shader!");
			e.printStackTrace();
			ARBShaderObjects.glDeleteObjectARB(shader);
		}
		return shader;
	}
	private static String log(int shader) {
		return ARBShaderObjects.glGetInfoLogARB(shader, ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
	private static String getData(String loc) {
		String path;

		try {
			path = ShaderManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		}
		catch (Exception e) {
			System.out.println("Failed to jar path!");
			e.printStackTrace();
			return null;
		}

		String data = "";

		if (path.endsWith(".jar")) {

			try {
				FileInputStream fis = new FileInputStream(path);
				ZipInputStream zis = new ZipInputStream(fis);
				ZipEntry entry;
				while ((entry = zis.getNextEntry()) != null) {
					if (entry.getName().equals(loc))
						break;
				}
				Scanner scanner = new Scanner(zis);
				while (scanner.hasNext()) {
					data += scanner.nextLine() + "\n";
				}
				scanner.close();
				fis.close();
				zis.close();

			}
			catch (Exception e) {
				System.out.println("Error while extracting!");
				e.printStackTrace();
			}

			return data;
		}
		else {

			try {
				FileInputStream fis = new FileInputStream(path + loc);
				Scanner scanner = new Scanner(fis);
				while (scanner.hasNext()) {
					data += scanner.nextLine() + "\n";
				}
				scanner.close();
				fis.close();
			}
			catch (Exception e) {
				System.out.println("Error while reading file!");
				e.printStackTrace();
			}

			return data;
		}

	}
}
