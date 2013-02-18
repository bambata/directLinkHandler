package com.floddle.applet;

import java.io.File;

public class FileChangeEvent {
	
	private File file;
	
	private String origin;

	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getOrigin() {
		return origin;
	}
	
}
