package com.rts.tap.serviceimplementation;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dao.ResumeBankMongoDao;
import com.rts.tap.exception.DocumentUploadException;
import com.rts.tap.exception.ResumeNotFoundException;
import com.rts.tap.exception.ResumeSaveException;
import com.rts.tap.model.ResumeBank;
import com.rts.tap.service.ResumeBankService;
import jakarta.transaction.Transactional;

@Service
@Transactional 
public class ResumeBankServiceImpl implements ResumeBankService {
    
    private final GridFsTemplate gridFsTemplate;
    private final ResumeBankMongoDao resumeBankMongoDao;
    private static final Logger logger = LoggerFactory.getLogger(ResumeBankServiceImpl.class);

    public ResumeBankServiceImpl(GridFsTemplate gridFsTemplate,
                                  ResumeBankMongoDao resumeBankMongoDao) {
        this.gridFsTemplate = gridFsTemplate;
        this.resumeBankMongoDao = resumeBankMongoDao;
    } 
 
    @Override
    public String addcandidateResume(MultipartFile candidateResume){
        logger.info(MessageConstants.ADDING_CANDIDATE_RESUME, candidateResume.getOriginalFilename());
        ResumeBank resumeBank = new ResumeBank();
        
        try {
            String fileId = gridFsTemplate.store(candidateResume.getInputStream(), 
                                                   candidateResume.getOriginalFilename(),
                                                   candidateResume.getContentType()).toString();
            resumeBank.setFileName(candidateResume.getOriginalFilename());
            resumeBank.setFileId(fileId);
            logger.info(MessageConstants.RESUME_ADDED_FOR_FILEID, fileId);
            return resumeBankMongoDao.save(resumeBank).getId();
        } catch (IOException e) {
            throw new DocumentUploadException(MessageConstants.FAILED_TO_UPLOAD_RESUME, e);
        }
    }

    @Override
    public List<ResumeBank> getAllResumeBank() {
    	 List<ResumeBank> resumeBank = resumeBankMongoDao.findAll();
 	    if (resumeBank.isEmpty()) {
 	        throw new ResumeNotFoundException(MessageConstants.RESUME_NOT_FOUND);
 	    }
    	
        logger.info(MessageConstants.FETCHING_ALL_RESUME);
        return resumeBank;
    }

    @Override
    public ResponseEntity<byte[]> getResumeById(String id) {
        logger.info(MessageConstants.FETCHING_RESUME_WITH_ID, id);
        ResumeBank resume = resumeBankMongoDao.findById(id)
                .orElseThrow(() -> new ResumeNotFoundException(MessageConstants.RESUME_NOT_FOUND_FOR_ID + id));
        String fileId = resume.getFileId();

        GridFSFile gridFSFile = gridFsTemplate.find(Query.query(Criteria.where("_id").is(fileId))).first();
        if (gridFSFile == null) {
            throw new ResumeNotFoundException(MessageConstants.RESUME_NOT_FOUND_FOR_ID+ fileId);
        }

        try {
            GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
            byte[] fileData = resource.getInputStream().readAllBytes();

            logger.info(MessageConstants.RESUME_RETRIEVED_SUCCESSFULLY,gridFSFile.getFilename());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(gridFSFile.getMetadata().getString("_contentType")))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + gridFSFile.getFilename() + "\"")
                    .body(fileData);
        } catch (IOException e) {
            throw new DocumentUploadException(MessageConstants.ERROR_FETCHING_RESUME, e);
        }
    }

    @Override
    public String updateResumeById(String resumeId, MultipartFile candidateresume) {
        logger.info(MessageConstants.UPDATING_RESUME_BY_ID, resumeId);
        
        ResumeBank resume = resumeBankMongoDao.findById(resumeId)
				.orElseThrow(() -> new ResumeNotFoundException(MessageConstants.RESUME_NOT_FOUND_FOR_ID  + resumeId));

        try {
            ObjectId newFileId = gridFsTemplate.store(candidateresume.getInputStream(), 
                                                        candidateresume.getOriginalFilename(),
                                                        candidateresume.getContentType());

            resume.setFileId(newFileId.toString());
            resume.setFileName(candidateresume.getOriginalFilename());
            logger.info(MessageConstants.UPDATING_RESUME_BY_ID, newFileId);
            return resumeBankMongoDao.save(resume).getId();
        } catch (IOException e) {
            throw new DocumentUploadException(MessageConstants.FAILED_TO_UPDATE_RESUME, e);
        }
    }
    
    @Override
	public String saveResume(ResumeBank resumeBank) {
		logger.info(MessageConstants.SAVING_RESUME_TO_BANK);
		try {
			resumeBankMongoDao.save(resumeBank);
	     	logger.info(MessageConstants.RESUME_SAVED_SUCCESSFULLY, resumeBank.getFileName());
	     	return MessageConstants.RESUME_SAVED_SUCCESSFULLY;
		} catch (Exception e) {
			throw new ResumeSaveException(MessageConstants.ERROR_ADDING_RESUME, e);
		}
	}

    @Override
    public String fetchPDFFromGoogleDrive(String link){
        logger.info(MessageConstants.FETCHING_PDF_FROM_GOOGLE_DRIVE, link);
        String fileName = extractFileIdFromLink(link);
        String fileDownloadLink = "https://drive.google.com/uc?export=download&id=" + fileName;

        try {
            URL url = new URL(fileDownloadLink);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (InputStream inputStream = conn.getInputStream()) {
                ResumeBank resumeBank = new ResumeBank();
                String contentType = "application/pdf";

                String file = gridFsTemplate.store(inputStream, "resume-" + fileName + ".pdf", contentType).toString();
                resumeBank.setFileName("resume-" + fileName + ".pdf");
                resumeBank.setFileId(file);
                resumeBankMongoDao.save(resumeBank);

                logger.info(MessageConstants.PDF_FETCHED_SUCCESSFULLY, resumeBank.getId());
                return resumeBank.getId();
            }
        } catch (IOException e) {
            throw new DocumentUploadException(MessageConstants.ERROR_FETCHING_PDF, e);
        }
    }

    private String extractFileIdFromLink(String link) {
        logger.info(MessageConstants.EXTRACT_FILE_ID_FROM_LINK, link);
        String[] parts = link.split("/");
        return parts[5]; 
    } 
}