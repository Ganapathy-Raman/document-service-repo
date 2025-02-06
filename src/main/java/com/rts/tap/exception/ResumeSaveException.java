package com.rts.tap.exception;

public class ResumeSaveException extends RuntimeException {
   
	private static final long serialVersionUID = 1L;

	public ResumeSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}