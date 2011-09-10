package com.unitedcaterers.db;

public class DuplicateKeyException extends Exception {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	public DuplicateKeyException() {
	}
	
	public DuplicateKeyException(String message) {
		super(message);
	}
	
	public DuplicateKeyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DuplicateKeyException(Throwable cause) {
		super(cause);
	}
}