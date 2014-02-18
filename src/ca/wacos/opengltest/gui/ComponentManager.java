package ca.wacos.opengltest.gui;

import ca.wacos.opengltest.Renderable;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glTranslatef;

public final class ComponentManager implements Renderable {

	private static ArrayList<ComponentManager> managers = new ArrayList<ComponentManager>();
	private ArrayList<Component> components = new ArrayList<Component>();

	public static void updateManagers() {
		for (ComponentManager manager : managers) {
			for (Component component : manager.components) {
				component.updatePosition();
			}
		}
	}

	public ComponentManager() {
		managers.add(this);
	}

	public void put(Component c) {
		components.add(c);
	}
	public void remove(Component c) {
		boolean removed;
		do
			removed = components.remove(c);
		while (removed);
	}
	public void render() {
		for (Component c : components) {
			glTranslatef(c.getX(), c.getY(), 0);
			c.render();
			glTranslatef(-c.getX(), -c.getY(), 0);
		}
	}
}
