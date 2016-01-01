package ru.mephi.dr.parser.util;

public class ParseException extends Exception {

	private static final long serialVersionUID = 458886872127274821L;

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParseException(String message) {
		super(message);
	}

}
