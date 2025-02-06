package com.rts.tap.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.rts.tap.model.ResumeBank;

public interface ResumeBankService {
	
	String addcandidateResume(MultipartFile candidateResume);
		
	List<ResumeBank> getAllResumeBank();
	
	ResponseEntity<byte[]> getResumeById(String id) ;
	
	String updateResumeById(String resumeId, MultipartFile candidateresume);
	
	public String saveResume(ResumeBank resumeBank);
	
	public String fetchPDFFromGoogleDrive(String link);
	 
	 

}