package ca.wacos.opengltest;

// This class wraps another object for multi-threaded use.
// Best used with a cloneable object.
public class SyncWrapper <T extends Object> {
	private T obj;
	public SyncWrapper(T obj) {
		this.obj = obj;
	}
	public synchronized void use(SyncRunnable<T> task) {
		task.run(obj);
	}
	@SuppressWarnings("unchecked")
	public synchronized T cloneObject() {
		if (obj instanceof Cloneable) {
			return (T) ((Cloneable) obj).clone();
		}
		else return null;
	}
	public interface SyncRunnable <T extends Object> {
		public void run(T object);
	}
}
