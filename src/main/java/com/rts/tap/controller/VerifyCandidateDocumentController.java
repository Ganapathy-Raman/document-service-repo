package com.rts.tap.controller;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rts.tap.constants.APIConstants;
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.model.CandidateDocument;
import com.rts.tap.service.VerifyCandidateDocumentService;
 
@RestController
@RequestMapping(path = APIConstants.BASE_URL)
public class VerifyCandidateDocumentController {

    private final VerifyCandidateDocumentService verifyCandidateDocumentService;
    private static final Logger logger = LoggerFactory.getLogger(VerifyCandidateDocumentController.class);

    public VerifyCandidateDocumentController(VerifyCandidateDocumentService verifyCandidateDocumentService) {
        this.verifyCandidateDocumentService = verifyCandidateDocumentService;
    }

    @GetMapping(path = APIConstants.RECRUITER_GET_DOCUMENTS_BY_CANDIDATE_ID)
    public Map<String, ResponseEntity<byte[]>> getDocumentsByCandidateId(@PathVariable("candidateId") long candidateId) {
        logger.info(MessageConstants.FETCHING_DOCUMENTS, candidateId);
        Map<String, ResponseEntity<byte[]>> documents = verifyCandidateDocumentService.fetchDocumentsByCandidateId(candidateId);
        logger.info(MessageConstants.DOCUMENTS_FETCHED_SUCCESSFULLY, candidateId);
        return documents;
    }

    @GetMapping(path = APIConstants.RECRUITER_GET_DOCUMENT_ID_BY_CANDIDATE_ID)
    public CandidateDocument getDocumentIdByCandidateId(@PathVariable("candidateId") long candidateId) {
        logger.info(MessageConstants.FETCHING_DOCUMENT_ID, candidateId);
        CandidateDocument document = verifyCandidateDocumentService.getDocumentByCandidateId(candidateId); 
        logger.info(MessageConstants.RETRIEVED_DOCUMENTS, candidateId);
        return document;
    }

    @PutMapping(path = APIConstants.UPDATE_CANDIDATE_DOCUMENT_STATUS)
    public ResponseEntity<String> updateCandidateDocumentStatus(@RequestBody CandidateDocument candidateDocument) {	   
        logger.info(MessageConstants.UPDATING_DOCUMENT_STATUS, candidateDocument.getCandidateDocumentId());
        verifyCandidateDocumentService.updateCandidateDocumentStatus(candidateDocument);
        logger.info(MessageConstants.DOCUMENT_STATUS_UPDATED_SUCCESSFULLY, candidateDocument.getCandidateDocumentId());
        return ResponseEntity.ok(MessageConstants.CANDIDATE_DOCUMENT_STATUS_UPDATE);	       
    }

    @PatchMapping(path = APIConstants.UPDATE_CANDIDATE_STATUS)
    public ResponseEntity<String> updateCandidateStatus(@PathVariable Long candidateId, @PathVariable String status) {	   
        logger.info(MessageConstants.UPDATING_CANDIDATE_STATUS, candidateId, status);
        verifyCandidateDocumentService.updateCandidateStatus(candidateId, status);
        logger.info(MessageConstants.CANDIDATE_STATUS_UPDATED_SUCCESSFULLY, candidateId);
        return ResponseEntity.ok(MessageConstants.CANDIDATE_STATUS_UPDATE+candidateId);	       
    }
}