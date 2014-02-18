package ca.wacos.opengltest;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.util.glu.GLU.gluLookAt;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class Renderer implements Renderable {

	private GameRenderer gameRenderer = new GameRenderer();
	private GUIRenderer guiRenderer = new GUIRenderer();
	private HUDRenderer hudRenderer = new HUDRenderer();

	private Matrix4f cameraMatrix = new Matrix4f();

	public void render() {

		GL11.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		GL11.glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
		glEnable(GL11.GL_COLOR_MATERIAL);

		ShaderManager.initialBuffer.bindFBO();
		updateCamera();

		draw3D();

		ShaderManager.initialBuffer.unbindFBO();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
		resetCamera();

		glClearColor(Vars.CLEAR_COLOR[0], Vars.CLEAR_COLOR[1], Vars.CLEAR_COLOR[2], Vars.CLEAR_COLOR[3]);
		glClearDepth(1);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_MODELVIEW);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glDisable(GL_LIGHTING);
		glLoadIdentity();

		ShaderManager.aoBuffer.bindFBO();
		ShaderManager.enableAmbientOcclusion();
		drawScene((float)Display.getWidth(), (float)Display.getHeight());
		ShaderManager.disableShader();
		ShaderManager.aoBuffer.unbindFBO();

		ShaderManager.enableFXAA();
		drawScene((float)Display.getWidth(), (float)Display.getHeight());
		ShaderManager.disableShader();

		glTranslatef(0, 0, -1);
		draw2D();

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(60.0f, (float)Display.getWidth()/(float)Display.getHeight(), Vars.ZNEAR, Vars.ZFAR);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glShadeModel(GL11.GL_SMOOTH);
		glClearColor(Vars.CLEAR_COLOR[0], Vars.CLEAR_COLOR[1], Vars.CLEAR_COLOR[2], Vars.CLEAR_COLOR[3]);
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glEnable(GL_LINE_SMOOTH);

		// Display.setVSyncEnabled(true);
		Display.update();
	}
	private void drawScene(float width, float height) {
		glColor3f(1, 1, 1);
		glBegin(GL_QUADS);

		glTexCoord2f(0, 0);
		glVertex3f(0, 0, 0);

		glTexCoord2f(1, 0);
		glVertex3f(width, 0, 0);

		glTexCoord2f(1, 1);
		glVertex3f(width, height, 0);

		glTexCoord2f(0, 1);
		glVertex3f(0, height, 0);

		glEnd();
	}
	public void updateCamera() {
		Vector renderPosition = RenderingManager.getRenderPosition();
		gluLookAt((float) renderPosition.getOrigin().getX(), (float) renderPosition.getOrigin().getY(), (float) renderPosition.getOrigin().getZ(),
				(float) renderPosition.relativeToOrigin().getX(), (float) renderPosition.relativeToOrigin().getY(), (float) renderPosition.relativeToOrigin().getZ(),
				0, 1, 0);
		cameraMatrix.m33 = 1;
		cameraMatrix.m30 = (float) renderPosition.getOrigin().getX();
		cameraMatrix.m31 = (float) renderPosition.getOrigin().getY();
		cameraMatrix.m32 = (float) renderPosition.getOrigin().getZ();
	}
	public void resetCamera() {
		gluLookAt(0, 0, 0, 0, 0,-1, 0, 1, 0);
	}
	public void draw3D() {
		gameRenderer.render();
		guiRenderer.render3D();
		hudRenderer.render3D();
	}
	public void draw2D() {
		guiRenderer.render2D();
		hudRenderer.render2D();
	}
	public HUDRenderer getHudRenderer() {
		return hudRenderer;
	}
	public Matrix4f getCameraMatrix() {
		return cameraMatrix;
	}
}
