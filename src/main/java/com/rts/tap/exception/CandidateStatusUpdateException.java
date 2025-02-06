package com.rts.tap.exception;

public class CandidateStatusUpdateException extends RuntimeException {
  
	private static final long serialVersionUID = 1L;

	public CandidateStatusUpdateException(String message) {
        super(message);
    }
}