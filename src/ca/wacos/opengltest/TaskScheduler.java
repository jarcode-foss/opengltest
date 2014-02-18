package ca.wacos.opengltest;

import java.util.HashMap;

public class TaskScheduler {
	private static HashMap<Integer, ScheduledTask> tasks = new HashMap<Integer, ScheduledTask>();
	public static synchronized void execute() {
		for (int id : tasks.keySet()) {
			ScheduledTask task = tasks.get(id);
			if (task.initialAt < task.initialDelay) {
				task.initialAt++;
			}
			else {
				if (task.repeat) {
					if (task.delayAt < task.tickDelay)
						task.delayAt++;
					else {
						try {
							task.task.run();
						}
						catch (Throwable e) {
							System.out.println("Error while executing repeating task: " + id);
						}
						task.delayAt = 0;
					}
				}
				else {
					try {
						task.task.run();
					}
					catch (Throwable e) {
						System.out.println("Error while executing task: " + id);
					}
					tasks.remove(id);
				}
			}
		}
	}
	public static synchronized int addTask(Runnable task) {
		int id = generateId();
		tasks.put(id, new ScheduledTask(task));
		return id;
	}
	public static synchronized int addDelayedTask(Runnable task, int delay) {
		int id = generateId();
		ScheduledTask t = new ScheduledTask(task);
		t.initialDelay = delay;
		tasks.put(id, t);
		return id;
	}
	public static synchronized int addRepeatingTask(Runnable task, int repeat) {
		int id = generateId();
		ScheduledTask t = new ScheduledTask(task);
		t.repeat = true;
		t.tickDelay = repeat;
		tasks.put(id, t);
		return id;
	}
	public static synchronized int addDelayedRepeatingTask(Runnable task, int delay, int repeat) {
		int id = generateId();
		ScheduledTask t = new ScheduledTask(task);
		t.repeat = true;
		t.tickDelay = repeat;
		t.initialDelay = delay;
		tasks.put(id, t);
		return id;
	}
	public static synchronized void removeTask(int id) {
		tasks.remove(id);
	}
	private static int generateId() {
		boolean found = false;
		int id = 0;
		while (!found) {
			found = true;
			for (int v : tasks.keySet()) {
				if (v == id) {
					found = false;
					break;
				}
			}
			if (!found) id++;
		}
		return id;
	}
	private static class ScheduledTask {
		Runnable task;
		boolean repeat = false;
		int tickDelay = 20;
		int initialDelay = 0;
		int initialAt = 0;
		int delayAt = 0;
		ScheduledTask(Runnable task) {
			this.task = task;
		}
	}
}
