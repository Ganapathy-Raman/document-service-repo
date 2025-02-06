package com.rts.tap.controller;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rts.tap.constants.APIConstants;
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dto.CandidateDocumentDto;
import com.rts.tap.service.CandidateDocumentService;

@RestController
@RequestMapping(path = APIConstants.BASE_URL)
public class CandidateDocumentController {

    private CandidateDocumentService candidateDocumentService;
    private static final Logger logger = LoggerFactory.getLogger(CandidateDocumentController.class);

    public CandidateDocumentController(CandidateDocumentService candidateDocumentService) {
        super();
        this.candidateDocumentService = candidateDocumentService;
    }
 
    @PostMapping(path = APIConstants.CANDIDATE_UPLOAD_DOCUMENTS, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadDocuments(@ModelAttribute CandidateDocumentDto candidateDocument) throws IOException {
        logger.info(MessageConstants.UPLOADING_DOCUMENTS, candidateDocument.getCandidateId());
        String response = candidateDocumentService.uploadDocuments(candidateDocument);
        logger.info(MessageConstants.DOCUMENTS_UPLOADED_SUCCESSFULLY, candidateDocument.getCandidateId());
        return ResponseEntity.ok(response); 
    } 

    @GetMapping(path = APIConstants.GET_DOCUMENTS_BY_CANDIDATEID)
    public CandidateDocumentDto getCandidateDocumentByCandidateId(@PathVariable Long candidateId) {
        logger.info(MessageConstants.FETCHING_DOCUMENTS, candidateId);
        CandidateDocumentDto candidateDocument = candidateDocumentService.getCandidateDocumentByCandidateId(candidateId);
        logger.info(MessageConstants.RETRIEVED_DOCUMENTS, candidateId);
        return candidateDocument;
    }

    @PutMapping(path = APIConstants.UPDATE_CANDIDATE_DOCUMENTS)
    public ResponseEntity<String> updateCandidateDocuments(@PathVariable Long candidateId,
            @ModelAttribute CandidateDocumentDto candidateDocumentDto) {
        logger.info(MessageConstants.UPDATING_DOCUMENTS, candidateId);
        String result = candidateDocumentService.updateCandidateDocuments(candidateDocumentDto);
        logger.info(MessageConstants.DOCUMENTS_UPDATED_SUCCESSFULLY, candidateId);
        return ResponseEntity.ok(result);
    }
}