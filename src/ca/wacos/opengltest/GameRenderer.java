package ca.wacos.opengltest;

import static org.lwjgl.opengl.GL11.*;

public class GameRenderer implements Renderable {
	public void render() {
		Map.render();
		glTranslatef(0, 2, 0);
		cube();
		glTranslatef(0, 0, 7);
		cube();
		glTranslatef(3, 0, 0);
		cube();
	}
	public void cube() {
		glBegin(GL_QUADS);							// Draw a cube with quads

		// glNormal3f(0, 2f, 0);
		glColor3f(0.0f,1.0f,0.0f);						// Color Blue
		glVertex3f( 1.0f, 1.0f,-1.0f);					// Top Right Of The Quad (Top)
		glVertex3f(-1.0f, 1.0f,-1.0f);					// Top Left Of The Quad (Top)
		glVertex3f(-1.0f, 1.0f, 1.0f);					// Bottom Left Of The Quad (Top)
		glVertex3f( 1.0f, 1.0f, 1.0f);					// Bottom Right Of The Quad (Top)

		// glNormal3f(0, -2f, 0);
		glColor3f(1.0f,0.5f,0.0f);						// Color Orange
		glVertex3f( 1.0f,-1.0f, 1.0f);					// Top Right Of The Quad (Bottom)
		glVertex3f(-1.0f,-1.0f, 1.0f);					// Top Left Of The Quad (Bottom)
		glVertex3f(-1.0f,-1.0f,-1.0f);					// Bottom Left Of The Quad (Bottom)
		glVertex3f( 1.0f,-1.0f,-1.0f);					// Bottom Right Of The Quad (Bottom)

		// glNormal3f(0, 0f, 2f);
		glColor3f(1.0f,0.0f,0.0f);						// Color Red
		glVertex3f( 1.0f, 1.0f, 1.0f);					// Top Right Of The Quad (Front)
		glVertex3f(-1.0f, 1.0f, 1.0f);					// Top Left Of The Quad (Front)
		glVertex3f(-1.0f,-1.0f, 1.0f);					// Bottom Left Of The Quad (Front)
		glVertex3f( 1.0f,-1.0f, 1.0f);					// Bottom Right Of The Quad (Front)

		// glNormal3f(0, 0f, 2f);
		glColor3f(1.0f,1.0f,0.0f);						// Color Yellow
		glVertex3f( 1.0f,-1.0f,-1.0f);					// Top Right Of The Quad (Back)
		glVertex3f(-1.0f,-1.0f,-1.0f);					// Top Left Of The Quad (Back)
		glVertex3f(-1.0f, 1.0f,-1.0f);					// Bottom Left Of The Quad (Back)
		glVertex3f( 1.0f, 1.0f,-1.0f);					// Bottom Right Of The Quad (Back)

		// glNormal3f(-2f, 0f, 0f);
		glColor3f(0.0f,0.0f,1.0f);						// Color Blue
		glVertex3f(-1.0f, 1.0f, 1.0f);					// Top Right Of The Quad (Left)
		glVertex3f(-1.0f, 1.0f,-1.0f);					// Top Left Of The Quad (Left)
		glVertex3f(-1.0f,-1.0f,-1.0f);					// Bottom Left Of The Quad (Left)
		glVertex3f(-1.0f,-1.0f, 1.0f);					// Bottom Right Of The Quad (Left)

		// glNormal3f(2f, 0f, 0f);
		glColor3f(1.0f,0.0f,1.0f);						// Color Violet
		glVertex3f( 1.0f, 1.0f,-1.0f);					// Top Right Of The Quad (Right)
		glVertex3f( 1.0f, 1.0f, 1.0f);					// Top Left Of The Quad (Right)
		glVertex3f( 1.0f,-1.0f, 1.0f);					// Bottom Left Of The Quad (Right)
		glVertex3f( 1.0f,-1.0f,-1.0f);					// Bottom Right Of The Quad (Right)
		glEnd();
	}
}
