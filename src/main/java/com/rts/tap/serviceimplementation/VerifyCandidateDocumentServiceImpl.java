package com.rts.tap.serviceimplementation;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dao.VerifyCandidateDocumentDao;
import com.rts.tap.dto.VerifyCandidateDocumentDto;
import com.rts.tap.exception.CandidateDocumentNotFoundException;
import com.rts.tap.exception.CandidateStatusUpdateException;
import com.rts.tap.exception.FileDownloadException;
import com.rts.tap.model.CandidateDocument;
import com.rts.tap.service.VerifyCandidateDocumentService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class VerifyCandidateDocumentServiceImpl implements VerifyCandidateDocumentService {

    private final VerifyCandidateDocumentDao verifyCandidateDocumentDao;
    private final GridFsTemplate gridFsTemplate;

    private static final Logger logger = LoggerFactory.getLogger(VerifyCandidateDocumentServiceImpl.class);

    public VerifyCandidateDocumentServiceImpl(VerifyCandidateDocumentDao verifyCandidateDocumentDao,
                                              GridFsTemplate gridFsTemplate) {
        this.verifyCandidateDocumentDao = verifyCandidateDocumentDao;
        this.gridFsTemplate = gridFsTemplate;
    }
  
    @Override 
    public CandidateDocument getDocumentByCandidateId(long candidateId) {
        logger.info(MessageConstants.RETRIEVED_DOCUMENTS, candidateId);
        CandidateDocument document = verifyCandidateDocumentDao.getDocumentByCandidateId(candidateId);
        if (document == null) {
            logger.error(MessageConstants.DOCUMENT_NOT_FOUND, candidateId);
            throw new CandidateDocumentNotFoundException(MessageConstants.DOCUMENT_NOT_FOUND + candidateId);
        }
        return document;
    }

    public Map<String, ResponseEntity<byte[]>> fetchDocumentsByCandidateId(long candidateId) {
        logger.info(MessageConstants.FETCHING_DOCUMENTS, candidateId);
        CandidateDocument document = getDocumentByCandidateId(candidateId);

        return fetchDocuments(document);
    }

    public Map<String, ResponseEntity<byte[]>> fetchDocuments(CandidateDocument document) {
        logger.info(MessageConstants.FETCHING_DOCUMENTS);
        Map<String, ResponseEntity<byte[]>> result = new HashMap<>();

        try {
            result.put("payslip", createResponseEntity(downloadFile(document.getPayslip())));
            result.put("experienceLetter", createResponseEntity(downloadFile(document.getExperienceLetter())));
            result.put("degreeCertificate", createResponseEntity(downloadFile(document.getDegreeCertificate())));
            result.put("aadhar", createResponseEntity(downloadFile(document.getAadhar())));
            result.put("panCard", createResponseEntity(downloadFile(document.getPanCard())));
            result.put("passport", createResponseEntity(downloadFile(document.getPassport())));
            result.put("relievingLetter", createResponseEntity(downloadFile(document.getRelievingLetter())));
        } catch (FileDownloadException e) {
            logger.error(MessageConstants.ERROR_FETCHING_DOCUMENTS, e.getMessage());
        }

        return result;
    }

    public ResponseEntity<byte[]> createResponseEntity(VerifyCandidateDocumentDto verifyCandidateDocumentDto) {
        if (verifyCandidateDocumentDto != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + verifyCandidateDocumentDto.getOriginalFilename() + "\"")
                    .body(verifyCandidateDocumentDto.getData());
        } else {
            logger.warn(MessageConstants.VERIFYCANDIDATEDOCUMENT_DTO_NOT_FOUND);
            return ResponseEntity.notFound().build();
        }
    }

    public VerifyCandidateDocumentDto downloadFile(String fileId) {
        logger.info(MessageConstants.DOWNLOADING_FILE, fileId);
        try {
            ObjectId objectId = new ObjectId(fileId);
            Query query = new Query(Criteria.where("_id").is(objectId));

            GridFsResource resource = gridFsTemplate.getResource(gridFsTemplate.findOne(query));

            if (resource.exists()) {
                try (InputStream inputStream = resource.getInputStream()) {
                    byte[] data = inputStream.readAllBytes();
                    String originalFilename = resource.getFilename();
                    
                    String contentType = resource.getContentType() != null && !resource.getContentType().isEmpty() 
        		            ? resource.getContentType() : null;
 
                    VerifyCandidateDocumentDto verifyCandidateDocumentDto = new VerifyCandidateDocumentDto();
                    verifyCandidateDocumentDto.setData(data);
                    verifyCandidateDocumentDto.setOriginalFilename(originalFilename);
                    verifyCandidateDocumentDto.setContentType(contentType);

                    logger.info(MessageConstants.DOWNLOADED_SUCCESSFULLY, originalFilename);
                    return verifyCandidateDocumentDto;
                }
            } else {
                logger.error(MessageConstants.FILE_NOT_FOUND, fileId);
                throw new FileDownloadException(MessageConstants.FILE_NOT_FOUND + fileId);
            }
        } catch (IOException e) {
            throw new FileDownloadException(MessageConstants.ERROR_DOWNLOADING_FILE + fileId, e);
        }
    }
    
    @Override
    public void updateCandidateDocumentStatus(CandidateDocument candidateDocument) {
        if (candidateDocument == null || candidateDocument.getCandidateDocumentId() == null) {
            logger.error(MessageConstants.CANDIDATE_DOCUMENT_NULL_ERROR);
            throw new CandidateStatusUpdateException(MessageConstants.CANDIDATE_DOCUMENT_NULL_ERROR);
        }

        logger.info(MessageConstants.UPDATING_DOCUMENT_STATUS, candidateDocument.getCandidateDocumentId());

        try {
            verifyCandidateDocumentDao.updateCandidateDocumentStatus(candidateDocument);
            logger.info(MessageConstants.CANDIDATE_DOCUMENT_UPDATE_SUCCESS, candidateDocument.getCandidateDocumentId());
        } catch (Exception e) {
            logger.error(MessageConstants.CANDIDATE_DOCUMENT_UPDATE_FAILURE, candidateDocument.getCandidateDocumentId(), e.getMessage());
            throw new CandidateStatusUpdateException(MessageConstants.CANDIDATE_DOCUMENT_UPDATE_FAILURE + candidateDocument.getCandidateDocumentId());
        }
    }

    @Override
    public void updateCandidateStatus(Long candidateId, String status) {
        if (candidateId == null) {
            logger.error(MessageConstants.CANDIDATE_ID_NULL_ERROR);
            throw new CandidateStatusUpdateException(MessageConstants.CANDIDATE_ID_NULL_ERROR);
        }
        
        logger.info(MessageConstants.UPDATING_CANDIDATE_STATUS, candidateId, status);
        try {
            verifyCandidateDocumentDao.updateCandidateStatus(candidateId, status);
            logger.info(MessageConstants.CANDIDATE_STATUS_UPDATE_SUCCESS, candidateId);
        } catch (Exception e) {
            logger.error(MessageConstants.CANDIDATE_STATUS_UPDATE_FAILURE, candidateId, e.getMessage());
            throw new CandidateStatusUpdateException(MessageConstants.CANDIDATE_STATUS_UPDATE_FAILURE + candidateId);
        }
    }
}



