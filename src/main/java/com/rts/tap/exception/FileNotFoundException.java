package com.rts.tap.exception;

public class FileNotFoundException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	public FileNotFoundException(String message) {
        super(message);
    }
}
