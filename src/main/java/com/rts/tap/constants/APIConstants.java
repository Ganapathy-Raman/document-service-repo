package com.rts.tap.constants;

public class APIConstants {

	private APIConstants() {
		throw new UnsupportedOperationException("APIConstants should not be instantiated");
	}

	public static final String BASE_URL = "/document-service/tap";
	public static final String FRONT_END_URL = "http://localhost:3000";

	// Verify Candidate Document
	public static final String RECRUITER_GET_DOCUMENTS_BY_CANDIDATE_ID = "/getDocumentByCandidateId/{candidateId}";
	public static final String RECRUITER_GET_DOCUMENT_ID_BY_CANDIDATE_ID = "/getDocumentIdByCandidateId/{candidateId}";
	public static final String UPDATE_CANDIDATE_DOCUMENT_STATUS = "/updateCandidateDocumentStatus";
	public static final String UPDATE_CANDIDATE_STATUS = "/updateCandidateStatus/{candidateId}/{status}";

	// Candidate Document
	public static final String CANDIDATE_UPLOAD_DOCUMENTS = "/uploadDocuments";
	public static final String UPDATE_CANDIDATE_DOCUMENTS = "/updateCandidateDocuments/{candidateId}";
	public static final String GET_DOCUMENTS_BY_CANDIDATEID = "/getCandidatedocument/{candidateId}";

	// Resume Bank
	public static final String ADD_CANDIDATE_RESUME = "/addResumeForCandidate";
	public static final String GET_ALL_CANDIDATE_RESUME = "/getAllCandidateResume";
	public static final String GET_CANDIDATE_RESUME_BY_CANDIDATE_ID = "/getCandidateResumeByResumeId/{resumeId}";
	public static final String UPDATE_RESUME_BY_ID = "/updateResume/{resumeId}";
	public static final String SAVE_RESUME_BANK = "/saveResumeBank";
	public static final String STORE_PDF_FROM_LINK = "/storepdf/googlelink";
}
