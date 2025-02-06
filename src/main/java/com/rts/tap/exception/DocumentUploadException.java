package com.rts.tap.exception;

public class DocumentUploadException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DocumentUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}