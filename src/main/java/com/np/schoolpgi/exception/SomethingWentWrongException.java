package com.np.schoolpgi.exception;

public class SomethingWentWrongException extends RuntimeException{

	/**
	 * 
	 */
//	private String message;
	
	private static final long serialVersionUID = 1L;

	
	public SomethingWentWrongException() {
		super();
	}

	public SomethingWentWrongException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SomethingWentWrongException(String message, Throwable cause) {
		super(message, cause);
	}

	public SomethingWentWrongException(String message) {
		super(message);
	}

	public SomethingWentWrongException(Throwable cause) {
		super(cause);
	}
	
	

}
