package com.rts.tap.exception;

public class FileDownloadException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	 public FileDownloadException(String message, Throwable cause) {
	        super(message, cause);
	  }
	 
	 public FileDownloadException(String message) {
		 super(message);
	}
}
