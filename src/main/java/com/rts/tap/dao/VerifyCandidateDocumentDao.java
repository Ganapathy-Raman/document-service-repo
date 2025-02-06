package com.rts.tap.dao;

import org.springframework.stereotype.Repository;

import com.rts.tap.model.CandidateDocument;

@Repository

public interface VerifyCandidateDocumentDao {

	CandidateDocument getDocumentByCandidateId(long candidateId);

	void updateCandidateDocumentStatus(CandidateDocument candiateDocument);

	void updateCandidateStatus(Long candidateId, String status);

}