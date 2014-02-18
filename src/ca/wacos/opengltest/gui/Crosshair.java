package ca.wacos.opengltest.gui;

import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Crosshair extends Component {
	@Override
	public void updatePosition() {
		setX(Display.getWidth() / 4);
		setY(Display.getHeight() / 4);
	}

	public void renderComponent() {
		glBegin(GL_POINTS);
		glColor3f(1.0f, 1.0f, 1.0f);
		glVertex2f(2.0f, 2.0f);
		glVertex2f(3.0f, 1.0f);
		glVertex2f(3.0f, 0.0f);
		glVertex2f(3.0f, -1.0f);
		glVertex2f(-2.0f, 2.0f);
		glVertex2f(-3.0f, 1.0f);
		glVertex2f(-3.0f, 0.0f);
		glVertex2f(-3.0f, -1.0f);
		glVertex2f(-2.0f, -2.0f);
		glVertex2f(1.0f, 3.0f);
		glVertex2f(0.0f, 3.0f);
		glVertex2f(-1.0f, 3.0f);
		glVertex2f(2.0f, -2.0f);
		glVertex2f(1.0f, -3.0f);
		glVertex2f(0.0f, -3.0f);
		glVertex2f(-1.0f, -3.0f);
		glEnd();
	}
}
