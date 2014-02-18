package ca.wacos.opengltest;

import org.lwjgl.LWJGLUtil;

import java.io.File;

public class OpenGLTest {
	public static void main(String args[]) {
		try {
			// Make sure LWJGL natives are there
			Installer.run();
			// Load them
			System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.home") + Vars.SEPARATOR + Vars.INSTALATION_DIR, "native"), getOS()).getAbsolutePath());
		}
		catch (Throwable e) {
			e.printStackTrace();
			// Error on initialization
			System.exit(2);
		}
		RenderingManager.init();
		System.exit(0);
	}
	public static String getOS() {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			return "windows";
		}
		else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			return "linux";
		}
		else if (System.getProperty("os.name").toLowerCase().contains("osx") || System.getProperty("os.name").toLowerCase().contains("mac")) {
			return "macosx";
		}
		else if (System.getProperty("os.name").toLowerCase().contains("solaris")) {
			return "solaris";
		}
		else return null;
	}
}
