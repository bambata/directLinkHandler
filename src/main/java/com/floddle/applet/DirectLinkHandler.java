package com.floddle.applet;

import java.applet.Applet;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;

public class DirectLinkHandler extends Applet implements FileChangeListener {

	Logger logger = LoggerFactory.getLogger(DirectLinkHandler.class);

	Sardine sardine = SardineFactory.begin();

	private File getFileFromRepository(String fileName) throws IOException {

		byte[] buffer = new byte[2048];

		String[] fileParts = fileName.split("\\.");

		File tempFile = File.createTempFile("foddle", "."
				+ fileParts[fileParts.length - 1]);
		FileOutputStream output = new FileOutputStream(tempFile);

		int size = 0;
		InputStream webdavFileContent = sardine.get(fileName);
		while ((size = webdavFileContent.read(buffer)) > 0) {
			output.write(buffer, 0, size);
		}
		output.flush();
		output.close();

		return tempFile;
	}

	public void openDirectLink(final String fileURL) {

		AccessController.doPrivileged(new PrivilegedAction<Void>() {

			public Void run() {

				try {
					
					File tempFile = getFileFromRepository(fileURL);

					tempFile.deleteOnExit();

					FileChangeNotifier notifier = new FileChangeNotifier();

					notifier.setService(new ScheduledThreadPoolExecutor(5));

					notifier.addListener(DirectLinkHandler.this);

					notifier.setFile(tempFile,
							fileURL);

					Desktop.getDesktop().open(tempFile);

				} catch (IOException e) {

					logger.error("Could not open file " + fileURL);

				}
				
				return null;
				
			}
		});

	}

	public void notifyChange(FileChangeEvent event) {

		File file = event.getFile();

		try {
			InputStream test = new FileInputStream(file);

			sardine.put(event.getOrigin(), test);

			logger.info(event.getOrigin() + " has been updated");

		} catch (FileNotFoundException e) {

			logger.warn("Could not find temp file " + e.getMessage());

		} catch (IOException e) {

			logger.warn("Could not read temp file " + e.getMessage());

		}

	}
}
