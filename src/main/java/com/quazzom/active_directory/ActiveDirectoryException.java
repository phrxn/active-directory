package com.quazzom.active_directory;

/**
 * Centralizes the errors that can occurs in this library
 *
 * @author david
 *
 */
public class ActiveDirectoryException extends Exception {

	public ActiveDirectoryException() {
		super();
	}

	public ActiveDirectoryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ActiveDirectoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActiveDirectoryException(String message) {
		super(message);
	}

	public ActiveDirectoryException(Throwable cause) {
		super(cause);
	}
}
