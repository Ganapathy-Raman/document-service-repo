package com.rts.tap.constants;

public class MessageConstants {
	
	private MessageConstants() {
		throw new UnsupportedOperationException("MessageConstants should not be instantiated");
	}
	
	public static final String UPLOADING_DOCUMENTS = "Uploading documents for candidate: ";
	public static final String DOCUMENTS_UPLOADED_SUCCESSFULLY = "Documents uploaded successfully for candidate ID: ";

	public static final String FETCHING_DOCUMENTS = "Fetching documents for candidate ID: ";
	public static final String RETRIEVED_DOCUMENTS = "Retrieved documents for candidate ID: ";

	public static final String UPDATING_DOCUMENTS = "Updating documents for candidate ID: ";
	public static final String DOCUMENTS_UPDATED_SUCCESSFULLY = "Documents updated successfully for candidate ID: ";
	public static final String ERROR_UPDATING_DOCUMENTS="Error updating candidate documents: ";
	
    public static final String DOCUMENTS_FETCHED_SUCCESSFULLY = "Documents fetched successfully for candidate ID: ";
    public static final String DOCUMENT_NOT_FOUND = "Candidate document not found for ID: ";
    public static final String ERROR_FETCHING_DOCUMENTS="Error fetching documents: "; 
    
    public static final String VERIFYCANDIDATEDOCUMENT_DTO_NOT_FOUND="VerifyCandidateDocumentDto is null. Returning 404 Not Found.";
    public static final String CANDIDATEDOCUMENT_DTO_NULL="Candidate document DTO is null.";
    public static final String DOWNLOADING_FILE="Downloading file with ID: ";
    public static final String  DOWNLOADED_SUCCESSFULLY="File downloaded successfully: ";
    public static final String  FILE_NOT_FOUND="File not found for ID: ";
    public static final String  ERROR_DOWNLOADING_FILE="Error while downloading file: ";
    public static final String  REUPLOADED="Reuploaded";
    public static final String  UPLOADED="Uploaded";
    
    public static final String FETCHING_DOCUMENT_ID = "Fetching document ID for candidate ID: ";
    
    public static final String UPDATING_DOCUMENT_STATUS = "Updating document status for candidate ID: ";
    public static final String DOCUMENT_STATUS_UPDATED_SUCCESSFULLY = "Document status updated successfully for candidate ID: ";
    
    public static final String UPDATING_CANDIDATE_STATUS = "Updating status for candidate to status: ";
    public static final String CANDIDATE_STATUS_UPDATED_SUCCESSFULLY = "Candidate status updated successfully for candidate ID: ";
    
    public static final String CANDIDATE_DOCUMENT_STATUS_UPDATE = "Candidate document status updated.";
    public static final String CANDIDATE_STATUS_UPDATE = "Candidate status updated for candidate Id :";
    
    public static final String DOCUMENT_RETRIEVED_SUCCESSFULLY = "Document retrieved successfully for candidate ID: ";
    
    public static final String ADDING_RESUME = "Adding resume for candidate: ";
    public static final String RESUME_ADDED_SUCCESSFULLY = "Resume added successfully for candidate: ";
    
    public static final String FETCHING_ALL_RESUMES = "Fetching all resumes from Resume Bank.";
    public static final String TOTAL_RESUMES_RETRIEVED = "Total resumes retrieved: ";
    
    public static final String FETCHING_RESUME_BY_ID = "Fetching resume with ID: ";
    public static final String RESUME_RETRIEVED_SUCCESSFULLY = "Resume retrieved successfully for ID: ";
    
    public static final String UPDATING_RESUME_BY_ID = "Updating resume with ID: ";
    public static final String RESUME_UPDATED_SUCCESSFULLY = "Resume updated successfully for ID: ";
    
    public static final String SAVING_RESUME_TO_BANK = "Saving resume to Resume Bank: ";
    public static final String RESUME_SAVED_SUCCESSFULLY = "Resume saved successfully: ";
    
    public static final String FETCHING_PDF_FROM_GOOGLE_DRIVE = "Fetching PDF from Google Drive with link: ";
    public static final String PDF_FETCHED_SUCCESSFULLY = "PDF fetched successfully from Google Drive";
    public static final String ERROR_FETCHING_PDF = "Error fetching PDF from Google Drive: ";
    public static final String EXTRACT_FILE_ID_FROM_LINK = "Error fetching PDF from Google Drive: ";
    
    public static final String ADDING_CANDIDATE_RESUME = "Adding candidate resume: ";
    public static final String RESUME_ADDED_FOR_FILEID = "Candidate resume added with file ID :";
    public static final String ERROR_ADDING_RESUME = "Error adding candidate resume: ";
    public static final String FAILED_TO_UPLOAD_RESUME = "Failed to upload candidate resume: ";
    public static final String FETCHING_ALL_RESUME = "Fetching all resumes from the resume bank.";
    public static final String FETCHING_RESUME_WITH_ID ="Fetching resume with ID: ";
    public static final String RESUME_NOT_FOUND_FOR_ID ="Resume not found for ID: ";
    public static final String RESUME_NOT_FOUND ="No resumes in the resume bank";
    public static final String ERROR_FETCHING_RESUME ="Error fetching resume data: ";
    public static final String ERROR_UPDATING_RESUME ="Error updating resume: ";
    public static final String FAILED_TO_UPDATE_RESUME = "Failed to update candidate resume: ";
    
    public static final String CANDIDATE_DOCUMENT_NULL_ERROR = "CandidateDocument or its ID is null";
    public static final String CANDIDATE_DOCUMENT_UPDATE_SUCCESS = "Candidate document status updated successfully for ID: ";
    public static final String CANDIDATE_DOCUMENT_UPDATE_FAILURE = "Failed to update candidate document status for ID: ";
    
    public static final String CANDIDATE_ID_NULL_ERROR = "Candidate ID cannot be null";
    public static final String CANDIDATE_STATUS_UPDATE_SUCCESS = "Candidate status updated successfully for ID: ";
    public static final String CANDIDATE_STATUS_UPDATE_FAILURE = "Failed to update candidate status for ID due to: ";
    
    public static final String UPLOADING_DOCUMENTS_INFO = "Uploading documents for candidate ID: ";
    public static final String DOCUMENT_UPLOAD_SUCCESS = "Documents uploaded successfully for candidate ID: ";
    public static final String RELIEVING_LETTER_MISSING_WARNING = "Relieving Letter is not provided.";
    public static final String PASSPORT_MISSING_WARNING = "Passport is not provided.";
    public static final String INVALID_ARGUMENT_ERROR = "Invalid argument while uploading documents";
    public static final String DATA_ACCESS_ERROR = "Error accessing data while uploading documents";
    public static final String GENERIC_UPLOAD_ERROR = "Error occurred during document upload";
    public static final String FILE_UPLOAD_INFO = "Uploading file:  with size ";
    public static final String FILE_NULL_OR_EMPTY_WARNING = "File is null or empty.";
    public static final String FILE_UPLOAD_ERROR = "Error occurred while uploading file: ";

}
