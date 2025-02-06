package com.rts.tap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalTap extends Throwable {

	private static final long serialVersionUID = 1L;

	@ExceptionHandler(CandidateDocumentNotFoundException.class)
	public ResponseEntity<String> handleCandidateDocumentNotFoundException(CandidateDocumentNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CandidateStatusUpdateException.class)
	public ResponseEntity<String> handleCandidateStatusUpdateException(CandidateStatusUpdateException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(FileDownloadException.class)
	public ResponseEntity<String> handleFileDownloadException(FileDownloadException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DocumentUploadException.class)
	public ResponseEntity<String> handleDocumentUploadException(DocumentUploadException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ResumeSaveException.class)
	public ResponseEntity<String> handleResumeSaveException(ResumeSaveException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ResumeNotFoundException.class)
	public ResponseEntity<String> handleResumeNotFoundException(ResumeNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	
}
