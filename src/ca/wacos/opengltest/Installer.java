package ca.wacos.opengltest;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.io.File;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static ca.wacos.opengltest.Vars.*;

public class Installer {

	private static long progress = 0;
	private static long fileSize = 0;
	private static int stage = 0;

	private static final Object lock = new Object();

	private static Thread downloadThread = null;

	private static boolean inProgress = false;

	private static HashMap<Integer, InstallationTask> tasks = new HashMap<Integer, InstallationTask>();

	static {
		addTask(new Installer.InstallationTask() {
			@Override
			public String getTitle() {
				return "Exracting LWJGL...";
			}

			@Override
			public void run() {
				String zipPath = System.getProperty("user.home") + Vars.SEPARATOR + "LWJGL.zip";
				try {
					new File(System.getProperty("user.home") + Vars.SEPARATOR + Vars.INSTALATION_DIR ).mkdir();
					File file = new File(zipPath);
					ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
					ZipEntry entry;
					while ((entry = zis.getNextEntry()) != null) {
						// Do not extract first level folders / files
						if (entry.getName().contains("/")) {
							String[] splitted = entry.getName().split("/");
							String path = "";
							if (splitted.length > 1 && !splitted[1].isEmpty()) {
								if (splitted[1].equals("native") || splitted[1].equals("jar")) {
									for (int t = 1; t < splitted.length; t++) {
										if (t == splitted.length - 1)
											path += splitted[t];
										else
											path += splitted[t] + Vars.SEPARATOR;
									}
									File outFile = new File(System.getProperty("user.home") + Vars.SEPARATOR + Vars.INSTALATION_DIR + Vars.SEPARATOR + path);
									if (!entry.isDirectory()) {
										new File(outFile.getParent()).mkdirs();
										FileOutputStream fos = new FileOutputStream(outFile);
										int bit;
										while ((bit = zis.read()) != -1) {
											fos.write(bit);
										}
										fos.close();
									}
								}
							}
						}
					}
					zis.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					new File(System.getProperty("user.home") + Vars.SEPARATOR + "LWJGL.zip").delete();
				}
			}
		});
	}

	public static boolean run() throws MultipleIntallationsError {
		if (new File(System.getProperty("user.home") + Vars.SEPARATOR + Vars.INSTALATION_DIR ).exists()) return false;
		if (!inProgress) {
			inProgress = true;
			start();
			displayProgressWindow();
			try {
				downloadThread.join();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			stage = 0;
			inProgress = false;
		}
		else {
			throw new MultipleIntallationsError();
		}
		System.out.println("completed installation");
		return true;
	}
	private static void displayProgressWindow() {
		JFrame frame = new JFrame();
		frame.setSize(400, 100);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setTitle(Vars.INSTALATION_DIR + " installation");
		frame.setVisible(true);
		JPanel panel = new JPanel(new BorderLayout());
		JPanel subPanel = new JPanel(new BorderLayout());
		JProgressBar bar = new JProgressBar();
		JLabel label = new JLabel("Downloading LWJGL...");
		JButton cancel = new JButton("Cancel");
		panel.add(bar, BorderLayout.CENTER);
		subPanel.add(label, BorderLayout.WEST);
		subPanel.add(cancel, BorderLayout.EAST);
		panel.add(subPanel, BorderLayout.NORTH);
		bar.setMinimum(0);
		bar.setMaximum(1000);
		bar.setStringPainted(true);
		frame.add(panel);

		try {
			while (true) {
				if (getStage() == 0) {
					bar.setValue((int) (getProgress() / (double) getFileSize() * 1000));
				}
				else if (getStage() == 1) {
					bar.setIndeterminate(true);
					label.setText("Extracting LWJGL...");
				}
				else if (getStage() == -1) {
					if (!downloadThread.isAlive()) break;
					bar.setIndeterminate(false);
					bar.setValue(1000);
					label.setText("Finished installation!");
					cancel.setEnabled(false);
				}
				else {
					bar.setIndeterminate(true);
					for (int key : tasks.keySet()) {
						if (key == getStage()) {
							label.setText(tasks.get(key).getTitle());
						}
					}
				}
				Thread.sleep(50);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		frame.setVisible(false);
	}
	private static void start() {
		downloadThread = new Thread(new Runnable() {
			public void run() {
				download();
				setStage(1);
				extract();
				for (int key : tasks.keySet()) {
					setStage(key);
					tasks.get(key).run();
				}
				setStage(-1);
				try {
					Thread.sleep(500);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		downloadThread.setDaemon(true);
		downloadThread.start();
	}
	public static void addTask(InstallationTask task) {
		boolean found = false;
		int t;
		for (t = 2; !found; t++) {
			found = true;
			for (int key : tasks.keySet())
				if (t == key) found = false;
		}
		tasks.put(t, task);
	}
	private static void extract() {

	}
	private static void download() {
		try {
			URL downloadLink = new URL(LWJGL_LINK);
			InputStream in = downloadLink.openStream();
			String dir = System.getProperty("user.home");
			FileOutputStream fos = new FileOutputStream(dir + "/lwjgl.zip");

			setFileSize(downloadLink.openConnection().getContentLength());

			int bit;
			while ((bit = in.read()) != -1) {
				incrementProgress();
				fos.write(bit);
			}

			in.close();
			fos.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void setProgress(long v) {
		synchronized (lock) {
			progress = v;
		}
	}
	private static void incrementProgress() {
		synchronized (lock) {
			progress += 1;
		}
	}
	private static void setFileSize(long v) {
		synchronized (lock) {
			fileSize = v;
		}
	}
	private static long getProgress() {
		synchronized (lock) {
			return progress;
		}
	}
	private static long getFileSize() {
		synchronized (lock) {
			return fileSize;
		}
	}
	private static void setStage(int v) {
		synchronized (lock) {
			stage = v;
		}
	}
	private static int getStage() {
		synchronized (lock) {
			return stage;
		}
	}
	private static class MultipleIntallationsError extends Throwable {

	}
	public static abstract class InstallationTask implements Runnable {
		public abstract String getTitle();
	}
}
