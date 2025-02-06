package com.rts.tap.exception;

public class CandidateDocumentNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public CandidateDocumentNotFoundException(String message) {
	        super(message);
	    }

}
