package com.rts.tap.service;

import java.io.IOException;
import com.rts.tap.dto.CandidateDocumentDto;

public interface CandidateDocumentService {

	public String uploadDocuments(CandidateDocumentDto candidateDocument) throws IOException;
	
	public CandidateDocumentDto getCandidateDocumentByCandidateId(Long candidateId);
	
	public String updateCandidateDocuments(CandidateDocumentDto candidateDocumentDto) ;
		 
}
