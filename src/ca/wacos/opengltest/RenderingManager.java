package ca.wacos.opengltest;

import ca.wacos.opengltest.gui.ComponentManager;
import ca.wacos.opengltest.triggers.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;

public class RenderingManager {

	private SyncWrapper<Vector> syncedRenderPosition = null;
	private Vector renderPosition = null;

	private static RenderingManager instance;
	private static final Object instanceLock = new Object();

	private Renderer renderer = new Renderer();

	private static long counter = 0;

	private static Thread mainThread = new Thread();
	private static final Object mainThreadLock = new Object();

	private static boolean mouseFree = true;
	private static int mouseX = 0;
	private static int mouseY = 0;

	public RenderingManager() {
		Vector vectorPos = new Vector(1, 0, 0);
		vectorPos.setOrigin(new Location(0, 10, 0));
		syncedRenderPosition = new SyncWrapper<Vector>(vectorPos);
	}

	public void start() {

		try {
			Display.setDisplayMode(new DisplayMode(1000,800));
			Display.setTitle("Test window");
			Display.create();
		} catch (LWJGLException e) {

			e.printStackTrace();
			System.exit(0);
		}

		createCameraControllers();
		InputHandler.init();
		ShaderManager.load();

		ComponentManager.updateManagers();

		Mouse.setGrabbed(true);

		while (!Display.isCloseRequested()) {
			long t1 = System.currentTimeMillis();
			renderPosition = syncedRenderPosition.cloneObject();
			renderer.render();
			long t2 = System.currentTimeMillis();

			counter += t2 - t1;
			if (counter >= 50) {
				counter = counter - 50;

				long t3 = System.currentTimeMillis();
				tick();
				long t4 = System.currentTimeMillis();
				if (t4 - t3 > 50) {
					System.out.println("WANRING: Cannot keep up! tick() method exceeded 50ms run time!");
				}
			}
		}

		Display.destroy();
	}
	public void tick() {
		TaskScheduler.execute();
	}
	public static void init() {
		mainThread = Thread.currentThread();
		instance = new RenderingManager();
		instance.start();
	}
	private void createCameraControllers() {

		new MouseMovementTrigger().attatch();

		new TriggerMoveCameraLeft().attatch(Keyboard.KEY_A);
		new TriggerMoveCameraRight().attatch(Keyboard.KEY_D);
		new TriggerMoveCameraForward().attatch(Keyboard.KEY_W);
		new TriggerMoveCameraBackward().attatch(Keyboard.KEY_S);
		new TriggerUnlockMouse().attatch(Keyboard.KEY_Q);
	}
	public static SyncWrapper<Vector> getSyncedRenderPosition() {
		synchronized (instanceLock) {
			return instance.syncedRenderPosition;
		}
	}
	public static Vector copyRenderPosition() {
		synchronized (instanceLock) {
			return instance.syncedRenderPosition.cloneObject();
		}
	}
	// Modifying this object will not affect the actual rendering position.
	public static Vector getRenderPosition() {
		checkThreading();
		return instance.renderPosition;
	}
	public static RenderingManager getInstance() {
		checkThreading();
		return instance;
	}
	private static void checkThreading() {
		synchronized (mainThreadLock) {
			if (Thread.currentThread() != mainThread) {
				throw new IllegalAccessError("Tried to access an unsynchronized method from a different thread");
			}
		}
	}
	public synchronized static void setMouseFree(boolean free) {
		mouseFree = free;
		InputHandler.resetMouse(free, Display.getWidth() / 2, Display.getHeight() / 2);
		if (free) {
			setMouseLocation(Display.getWidth() / 2, Display.getHeight() / 2);
		}
	}
	public synchronized static boolean isMouseFree() {
		return mouseFree;
	}
	public synchronized static void setMouseLocation(int x, int y) {
		mouseX = x;
		mouseY = y;
		instance.renderer.getHudRenderer().updateMouse(x, y);
	}
	public synchronized static int getMouseX() {
		return mouseX;
	}
	public synchronized static int getMouseY() {
		return mouseY;
	}
}
