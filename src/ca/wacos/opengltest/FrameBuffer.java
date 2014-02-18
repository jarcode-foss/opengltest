package ca.wacos.opengltest;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class FrameBuffer {

	private int framebufferID;
	private int colorTextureID;
	private int depthTextureID;

	public int getDepthTextureID() {
		return depthTextureID;
	}
	public int getColorTextureID() {
		return colorTextureID;
	}
	public int getFramebufferID() {
		return framebufferID;
	}

	public void setupFBO() {
		if (!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
			System.out.println("FBOs not supported!");
			System.exit(0);
		}
		else {

			framebufferID = glGenFramebuffersEXT();

			colorTextureID = glGenTextures();
			depthTextureID = glGenTextures();

			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);

			glBindTexture(GL_TEXTURE_2D, colorTextureID);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, Display.getWidth(), Display.getHeight(), 0,GL_RGBA, GL_FLOAT, (java.nio.ByteBuffer) null);

			glBindTexture(GL_TEXTURE_2D, depthTextureID);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT24, Display.getWidth(), Display.getHeight(), 0,GL_DEPTH_COMPONENT, GL_FLOAT, (java.nio.ByteBuffer) null);

			glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_TEXTURE_2D, depthTextureID, 0);
			glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL_TEXTURE_2D, colorTextureID, 0);

		}
	}
	public void bindFBO() {
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);

		glClearColor(Vars.CLEAR_COLOR[0], Vars.CLEAR_COLOR[1], Vars.CLEAR_COLOR[2], Vars.CLEAR_COLOR[3]);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	public void unbindFBO() {
		glEnable(GL_TEXTURE_2D);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

		glClearColor (Vars.CLEAR_COLOR[0], Vars.CLEAR_COLOR[1], Vars.CLEAR_COLOR[2], Vars.CLEAR_COLOR[3]);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
}
