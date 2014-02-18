package ca.wacos.opengltest.gui;

import ca.wacos.opengltest.Renderable;

import static org.lwjgl.opengl.GL11.glTranslatef;

public abstract class Component implements Renderable {
	private int x = 0;
	private int y = 0;
	public void render() {
		renderComponent();
	}
	public final void setX(int x) {
		this.x = x;
	}
	public final void setY(int y) {
		this.y = y;
	}
	public final int getX() {
		return x;
	}
	public final int getY() {
		return y;
	}
	public abstract void updatePosition();
	public abstract void renderComponent();
}
