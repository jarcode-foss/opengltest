package ca.wacos.opengltest;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class RenderUtils {
	public static FloatBuffer floatBuffer(float a, float b, float c, float d) {
		float[] data = new float[]{ a, b, c, d};
		FloatBuffer fb = BufferUtils.createFloatBuffer(data.length);
		fb.put(data);
		fb.flip();
		return fb;
	}
}
