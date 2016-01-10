package ru.mephi.dr.parser.exception;

/**
 * This exception is thrown when document doesn't satisfy validation rules.
 * 
 * @author Григорий
 *
 */
public class ValidateException extends Exception {

	private static final long serialVersionUID = 8720719992299694639L;

	public ValidateException(String message) {
		super(message);
	}

}
