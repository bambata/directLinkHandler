package com.floddle.applet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FileChangeNotifier {

	private List<FileChangeListener> listeners = new ArrayList<FileChangeListener>();

	private ScheduledExecutorService service;

	private File file;

	private long timeStamp;

	private String origin;

	private ScheduledFuture<?> futureJob;

	public void setService(ScheduledExecutorService service) {
		this.service = service;
	}

	private boolean isFileUpdated(File file) {
		long newTimeStamp = file.lastModified();

		if (this.timeStamp != 0 && newTimeStamp != this.timeStamp) {
			this.timeStamp = newTimeStamp;
			return true;
		}

		this.timeStamp = newTimeStamp;

		return false;
	}

	public void addListener(FileChangeListener listener) {

		listeners.add(listener);

	}

	public void setFile(File file, String origin) {

		if (file != null && file.exists()) {

			this.file = file;

			this.origin = origin;

			if (service != null) {

				if (futureJob != null)
					futureJob.cancel(false);

				futureJob = service.scheduleAtFixedRate(new Runnable() {

					public void run() {

						for (FileChangeListener listener : listeners) {
							if (isFileUpdated(FileChangeNotifier.this.file)) {
								FileChangeEvent event = new FileChangeEvent();
								event.setFile(FileChangeNotifier.this.file);
								event.setOrigin(FileChangeNotifier.this.origin);
								listener.notifyChange(event);
							}
						}

					}
				}, 0, 10, TimeUnit.SECONDS);
			}

		}
	}

}
