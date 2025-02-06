package com.rts.tap.dao;

import org.springframework.stereotype.Repository;

import com.rts.tap.model.Candidate;
import com.rts.tap.model.CandidateDocument;

@Repository
public interface CandidateDocumentDao {

	public CandidateDocument saveDocuments(CandidateDocument candiateDocument);

	public Candidate findById(Long id);

	void update(Candidate candidate);

	public CandidateDocument getCandidateDocumentByCandidateId(Long candidateId);

	public CandidateDocument updateCandidateDocuments(CandidateDocument existingDocument);

}
