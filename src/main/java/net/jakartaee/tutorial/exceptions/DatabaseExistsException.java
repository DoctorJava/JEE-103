package net.jakartaee.tutorial.exceptions;

public class DatabaseExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	public DatabaseExistsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DatabaseExistsException(String arg0) {
		super(arg0);
	}

}
