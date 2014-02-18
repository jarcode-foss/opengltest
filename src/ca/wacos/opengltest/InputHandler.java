package ca.wacos.opengltest;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InputHandler implements Runnable {

	private static HashMap<Integer, ArrayList<KeyboardTrigger>> triggers = new HashMap<Integer, ArrayList<KeyboardTrigger>>();
	private static HashMap<Integer, ArrayList<MouseTrigger>> mouseTriggers = new HashMap<Integer, ArrayList<MouseTrigger>>();
	private static ArrayList<MouseMovementListener> movementListeners = new ArrayList<MouseMovementListener>();

	private static boolean initialized = false;
	private static InputHandler instance = new InputHandler();
	private static Thread thread = new Thread(instance);

	private int mx = 0;
	private int my = 0;

	private int omx = 0;
	private int omy = 0;

	public static void init() {
		if (!initialized) {
			thread.setDaemon(true);
			thread.start();
		}
	}
	private InputHandler() {

	}
	public void run() {
		try {

			while (true) {

				long t1 = System.currentTimeMillis();

				synchronized (InputHandler.class) {

					if (!RenderingManager.isMouseFree()) {
						mx -= Mouse.getX() - (Display.getWidth() / 2);
						my += Mouse.getY() - (Display.getHeight() / 2);

						if (my > Display.getHeight()) {
							my = Display.getHeight();
						}
						else if (my < -Display.getHeight()) {
							my = -Display.getHeight();
						}
					}
					else {
						mx = Mouse.getX();
						my = Mouse.getY();

						if (my > Display.getHeight()) {
							my = Display.getHeight();
						}
						else if (my < 0) {
							my = 0;
						}
						if (mx > Display.getWidth()) {
							mx = Display.getWidth();
						}
						else if (mx < 0) {
							mx = 0;
						}
					}

					while (Mouse.next()) {
						ArrayList<MouseTrigger> list = mouseTriggers.get(Mouse.getEventButton());
						if (list != null) {
							for (MouseTrigger trigger : list) {
								if (Mouse.getEventButtonState())
									trigger.onPress();
								else
									trigger.onRelease();
							}
						}
					}

					while (Keyboard.next()) {
						ArrayList<KeyboardTrigger> list = triggers.get(Keyboard.getEventKey());
						if (list != null) {
							for (KeyboardTrigger trigger : list) {
								if (Keyboard.getEventKeyState())
									trigger.onPress();
								else
									trigger.onRelease();
							}
						}
					}

					if (Mouse.getX() != Display.getWidth() / 2 || Mouse.getY() != Display.getHeight() / 2) {
						for (MouseMovementListener listener : movementListeners) {
							listener.moved(mx, my, Mouse.getX() - (Display.getWidth() / 2), Mouse.getY() - (Display.getHeight() / 2));
						}
					}

					for (ArrayList<KeyboardTrigger> list : triggers.values()) {
						if (list != null) {
							for (KeyboardTrigger trigger : list) {
								trigger.update();
							}
						}
					}
					for (ArrayList<MouseTrigger> list : mouseTriggers.values()) {
						if (list != null) {
							for (MouseTrigger trigger : list) {
								trigger.update();
							}
						}
					}
					for (MouseMovementListener listener : movementListeners) {
						listener.update();
					}

					if (!RenderingManager.isMouseFree())
						Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
				}

				long t2 = System.currentTimeMillis();

				Thread.sleep(20 - ((t2 - t1) / 1000));
			}
		}
		catch (Exception e) {
			System.out.println("Input thread crashed!");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static synchronized void addKeyboardTrigger(int action, KeyboardTrigger task) {
		if (triggers.containsKey(action))
			triggers.get(action).add(task);
		else
			triggers.put(action, new ArrayList<KeyboardTrigger>(Arrays.asList(task)));
	}
	public static synchronized void removeKeyboardTrigger(KeyboardTrigger task) {
		for (int action : triggers.keySet())
			removeKeyboardTrigger(action, task);
	}
	public static synchronized void removeKeyboardTrigger(int action, KeyboardTrigger task) {
		boolean removed;
		do
			removed = triggers.get(action).remove(task);
		while (removed);
	}
	public static synchronized void addMouseTrigger(int action, MouseTrigger task) {
		if (mouseTriggers.containsKey(action))
			mouseTriggers.get(action).add(task);
		else
			mouseTriggers.put(action, new ArrayList<MouseTrigger>(Arrays.asList(task)));
	}
	public static synchronized void removeMouseTrigger(MouseTrigger task) {
		for (int action : mouseTriggers.keySet())
			removeMouseTrigger(action, task);
	}
	public static synchronized void removeMouseTrigger(int action, MouseTrigger task) {
		boolean removed;
		do
			removed = mouseTriggers.get(action).remove(task);
		while (removed);
	}
	public static synchronized void addMouseMovementListener(MouseMovementListener listener) {
		movementListeners.add(listener);
	}
	public static synchronized void removeMouseMovementListener(MouseMovementListener listener) {
		boolean removed;
		do
			removed = movementListeners.remove(listener);
		while (removed);
	}
	public static synchronized void resetMouse(boolean free, int midX, int midY) {
		if (free) {
			instance.omx = instance.mx;
			instance.omy = instance.my;

			instance.mx = midX;
			instance.my = midY;
		}
		else {
			instance.mx = instance.omx;
			instance.my = instance.omy;
		}
	}

	public interface KeyboardTrigger {
		public void onPress();
		public void onRelease();
		public void update();
	}
	public interface MouseTrigger {
		public void onPress();
		public void onRelease();
		public void update();
	}
	public interface MouseMovementListener {
		public void moved(int x, int y, int xd, int yd);
		public void update();
	}
}
