package ca.wacos.opengltest;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class Map {

	public static void render() {
		renderFloor();
	}
	private static void renderFloor() {
		glBegin(GL_QUADS);
		GL11.glColor3f(1f, 1f, 1f);

		GL11.glVertex3f( 128.0f, -1.0f,-128.0f);
		GL11.glVertex3f(-128.0f, -1.0f,-128.0f);
		GL11.glVertex3f(-128.0f, -1.0f, 128.0f);
		GL11.glVertex3f( 128.0f, -1.0f, 128.0f);

		GL11.glVertex3f( 128.0f, 0f,-128.0f);
		GL11.glVertex3f(-128.0f, 0f,-128.0f);
		GL11.glVertex3f(-128.0f, 0f, 128.0f);
		GL11.glVertex3f( 128.0f, 0f, 128.0f);

		GL11.glVertex3f( 128.0f, 0f,-128.0f);
		GL11.glVertex3f( 128.0f, -1f,-128.0f);
		GL11.glVertex3f( 128.0f, -1f, 128.0f);
		GL11.glVertex3f( 128.0f, 0f, 128.0f);

		GL11.glVertex3f(-128.0f, 0f,-128.0f);
		GL11.glVertex3f(-128.0f, -1f,-128.0f);
		GL11.glVertex3f(-128.0f, -1f, 128.0f);
		GL11.glVertex3f(-128.0f, 0f, 128.0f);

		GL11.glVertex3f(-128.0f, 0f, -128.0f);
		GL11.glVertex3f(-128.0f, -1f, -128.0f);
		GL11.glVertex3f( 128.0f, -1f, -128.0f);
		GL11.glVertex3f( 128.0f, 0f, -128.0f);

		GL11.glVertex3f(-128.0f, 0f, 128.0f);
		GL11.glVertex3f(-128.0f, -1f, 128.0f);
		GL11.glVertex3f( 128.0f, -1f, 128.0f);
		GL11.glVertex3f( 128.0f, 0f, 128.0f);
		glEnd();
	}
}