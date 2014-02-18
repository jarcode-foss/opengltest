package ca.wacos.opengltest.triggers;

import ca.wacos.opengltest.InputHandler;
import ca.wacos.opengltest.RenderingManager;
import ca.wacos.opengltest.SyncWrapper;
import ca.wacos.opengltest.Vector;

public class TriggerMoveCameraRight extends PredefinedKeyTrigger {

	public InputHandler.KeyboardTrigger getHandler() {
		return new InputHandler.KeyboardTrigger() {
			boolean down = false;
			@Override
			public void onPress() {
				down = true;
			}

			@Override
			public void onRelease() {
				down = false;
			}

			@Override
			public void update() {
				if (down) {
					final Vector position = RenderingManager.getSyncedRenderPosition().cloneObject();

					double xd = position.getX() / 4;
					double zd = position.getZ() / 4;

					double v = position.getOrigin().getX() - zd;
					position.getOrigin().setX(v);
					v = position.getOrigin().getZ() + xd;
					position.getOrigin().setZ(v);

					RenderingManager.getSyncedRenderPosition().use(new SyncWrapper.SyncRunnable<Vector>() {
						@Override
						public void run(Vector object) {
							object.copy(position);
						}
					});
				}
			}
		};
	}
}
