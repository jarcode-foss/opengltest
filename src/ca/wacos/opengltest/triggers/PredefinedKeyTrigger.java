package ca.wacos.opengltest.triggers;

import ca.wacos.opengltest.InputHandler;

public abstract class PredefinedKeyTrigger {
	public void attatch(int key) {
		InputHandler.addKeyboardTrigger(key, getHandler());
	}
	public abstract InputHandler.KeyboardTrigger getHandler();
}
