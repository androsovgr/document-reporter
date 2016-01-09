package ru.mephi.dr.exception;

import java.io.IOException;

public class DirectoryException extends IOException {

	private static final long serialVersionUID = -218850015101652877L;

	public DirectoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public DirectoryException(String message) {
		super(message);
	}

}
