package ca.wacos.opengltest.triggers;

import ca.wacos.opengltest.InputHandler;
import ca.wacos.opengltest.RenderingManager;
import ca.wacos.opengltest.SyncWrapper;
import ca.wacos.opengltest.Vector;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class MouseMovementTrigger {
	public void attatch() {
		InputHandler.addMouseMovementListener(new InputHandler.MouseMovementListener() {

			@Override
			public void moved(int mx, int my, int xd, int yd) {
				if (!RenderingManager.isMouseFree()) {
					double radx = (((2 * Math.PI) / 3000) * mx) % (Math.PI * 2);
					double rady = (((2 * Math.PI) / 3000) * my) % (Math.PI * 2);

					if (rady > Math.PI / 2d - 0.001) {
						rady = Math.PI / 2d - 0.001;
						my -= Mouse.getY() - (Display.getHeight() / 2);
					} else if (rady < Math.PI / -2d + 0.001) {
						rady = Math.PI / -2d + 0.001;
						my -= Mouse.getY() - (Display.getHeight() / 2);
					}

					double x = Math.sin(radx) * Math.cos(rady);
					double z = Math.cos(radx) * Math.cos(rady);
					double y = Math.sin(rady);

					final Vector position = RenderingManager.getSyncedRenderPosition().cloneObject();

					position.setX(x);
					position.setY(y);
					position.setZ(z);

					RenderingManager.getSyncedRenderPosition().use(new SyncWrapper.SyncRunnable<Vector>() {
						@Override
						public void run(Vector object) {
							object.copy(position);
						}
					});
				}
				else {
					RenderingManager.setMouseLocation(mx, my);
				}
			}

			@Override
			public void update() {
			}
		});
	}
}
