package ca.wacos.opengltest;

import java.nio.file.FileSystems;

public class Vars {
	public static final String INSTALATION_DIR = "OpenGLTest";
	public static final String SEPARATOR = FileSystems.getDefault().getSeparator();
	public static final String LWJGL_LINK = "http://ci.newdawnsoftware.com/job/LWJGL-git-dist/lastSuccessfulBuild/artifact/dist/lwjgl-2.9.1.zip";
	public static final float ZNEAR = 1f;
	public static final float ZFAR = 200f;
	public static final float[] CLEAR_COLOR = {0.8f, 0.8f, 0.8f, 0};
	private Vars() {}
}
