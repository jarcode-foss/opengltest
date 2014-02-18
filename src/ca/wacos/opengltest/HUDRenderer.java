package ca.wacos.opengltest;

import ca.wacos.opengltest.gui.ComponentManager;
import ca.wacos.opengltest.gui.*;

public class HUDRenderer {

	private ComponentManager manager = new ComponentManager();
	private Mouse mouse = new Mouse();
	private static volatile boolean enabled = true;

	HUDRenderer() {
		manager.put(mouse);
	}

	public static void enable() {
		enabled = true;
	}
	public static void disable() {
		enabled = false;
	}

	public void render2D() {
		manager.render();
	}
	public void render3D() {
		if (enabled) {

		}
	}
	public void updateMouse(int x, int y) {
		mouse.setX(x);
		mouse.setY(y);
	}
}
