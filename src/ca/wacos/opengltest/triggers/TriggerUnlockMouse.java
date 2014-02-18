package ca.wacos.opengltest.triggers;

import ca.wacos.opengltest.InputHandler;
import ca.wacos.opengltest.RenderingManager;

public class TriggerUnlockMouse extends PredefinedKeyTrigger {

	public InputHandler.KeyboardTrigger getHandler() {
		return new InputHandler.KeyboardTrigger() {
			@Override
			public void onPress() {
				RenderingManager.setMouseFree(!RenderingManager.isMouseFree());
			}

			@Override
			public void onRelease() {
			}

			@Override
			public void update() {
			}
		};
	}
}
