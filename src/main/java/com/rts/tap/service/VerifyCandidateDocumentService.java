package com.rts.tap.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.rts.tap.model.CandidateDocument;

public interface VerifyCandidateDocumentService {

	public CandidateDocument getDocumentByCandidateId(long candidateId);

	public Map<String, ResponseEntity<byte[]>> fetchDocumentsByCandidateId(long candidateId);

	public void updateCandidateStatus(Long candidateId, String status);

	public void updateCandidateDocumentStatus(CandidateDocument candidateDocument);

}