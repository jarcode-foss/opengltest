package ca.wacos.opengltest;

public class GUIRenderer {

	private static volatile boolean enabled = false;

	public static void enable() {
		enabled = true;
	}
	public static void disable() {
		enabled = false;
	}

	public void render2D() {
		if (enabled) {

		}
	}
	public void render3D() {
		if (enabled) {

		}
	}
}
