package com.rts.tap.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.rts.tap.constants.APIConstants;
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.model.ResumeBank;
import com.rts.tap.service.ResumeBankService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping(path = APIConstants.BASE_URL)
public class ResumeBankController {
   
    private final ResumeBankService resumeBankService;
    private static final Logger logger = LoggerFactory.getLogger(ResumeBankController.class);

    public ResumeBankController(ResumeBankService resumeBankService) {
        this.resumeBankService = resumeBankService;
    }

    @PostMapping(path = APIConstants.ADD_CANDIDATE_RESUME, consumes = "multipart/form-data")
    public String addResume(@RequestParam("candidateResume") MultipartFile candidateResume) {
        logger.info(MessageConstants.ADDING_RESUME, candidateResume.getOriginalFilename());
        String response = resumeBankService.addcandidateResume(candidateResume);
        logger.info(MessageConstants.RESUME_ADDED_SUCCESSFULLY, candidateResume.getOriginalFilename());
        return response;
    }

    @GetMapping(path = APIConstants.GET_ALL_CANDIDATE_RESUME)
    public List<ResumeBank> getAllResume() {
        logger.info(MessageConstants.FETCHING_ALL_RESUMES);
        List<ResumeBank> resumes = resumeBankService.getAllResumeBank();
        logger.info(MessageConstants.TOTAL_RESUMES_RETRIEVED, resumes.size());
        return resumes;
    }

    @GetMapping(path = APIConstants.GET_CANDIDATE_RESUME_BY_CANDIDATE_ID)
    public ResponseEntity<byte[]> getResumeById(@PathVariable String resumeId) {
        logger.info(MessageConstants.FETCHING_RESUME_BY_ID, resumeId);
        ResponseEntity<byte[]> resumeData = resumeBankService.getResumeById(resumeId);
        logger.info(MessageConstants.RESUME_RETRIEVED_SUCCESSFULLY, resumeId);
        return resumeData;
    }

    @PutMapping(path = APIConstants.UPDATE_RESUME_BY_ID)
    public String updateResumeById(@PathVariable String resumeId, @RequestParam("candidateResume") MultipartFile candidateresume) {
        logger.info(MessageConstants.UPDATING_RESUME_BY_ID, resumeId);
        String response = resumeBankService.updateResumeById(resumeId, candidateresume);
        logger.info(MessageConstants.RESUME_UPDATED_SUCCESSFULLY, resumeId);
        return response;
    }

    @PostMapping(path = APIConstants.SAVE_RESUME_BANK)
    public String saveResume(@RequestBody ResumeBank resumeBank) {
        logger.info(MessageConstants.SAVING_RESUME_TO_BANK, resumeBank);
		return resumeBankService.saveResume(resumeBank);
    }

    @PostMapping(path = APIConstants.STORE_PDF_FROM_LINK)
    public String fetchPDFFromGoogleDrive(@RequestParam String link) {
        logger.info(MessageConstants.FETCHING_PDF_FROM_GOOGLE_DRIVE, link);
        String response = resumeBankService.fetchPDFFromGoogleDrive(link);
        logger.info(MessageConstants.PDF_FETCHED_SUCCESSFULLY);
        return response;
    }
}