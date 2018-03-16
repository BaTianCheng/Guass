package com.esb.guass.test;

import java.io.IOException;

public class TestFileHandler extends java.util.logging.FileHandler {

	public TestFileHandler(String pattern, int limit, int count, boolean append) throws IOException, SecurityException {
		super(pattern, limit, count, append);
	}

	public TestFileHandler(String pattern, int limit, int count) throws IOException, SecurityException {
		super(pattern, limit, count);
	}

	public TestFileHandler(String pattern, boolean append) throws IOException, SecurityException {
		super(pattern, append);
	}

	public TestFileHandler() throws IOException, SecurityException {
		super();
	}

	public TestFileHandler(String pattern) throws IOException, SecurityException {
		super(pattern);
	}

}
