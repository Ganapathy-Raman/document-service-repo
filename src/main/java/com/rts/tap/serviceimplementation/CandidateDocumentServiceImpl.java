package com.rts.tap.serviceimplementation;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.common.net.HttpHeaders;
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dao.CandidateDocumentDao;
import com.rts.tap.dto.CandidateDocumentDto;
import com.rts.tap.exception.CandidateDocumentNotFoundException;
import com.rts.tap.exception.DocumentUploadException;
import com.rts.tap.exception.FileNotFoundException;
import com.rts.tap.model.Candidate;
import com.rts.tap.model.CandidateDocument;
import com.rts.tap.service.CandidateDocumentService;
import jakarta.transaction.Transactional;
  
@Service
@Transactional
public class CandidateDocumentServiceImpl implements CandidateDocumentService {
	private final CandidateDocumentDao candidateDocumentDao;
	private final GridFsTemplate gridFsTemplate;

	private static final Logger logger = LoggerFactory.getLogger(CandidateDocumentServiceImpl.class);

	public CandidateDocumentServiceImpl(CandidateDocumentDao candidateDocumentDao, GridFsTemplate gridFsTemplate) {
		this.candidateDocumentDao = candidateDocumentDao; 
		this.gridFsTemplate = gridFsTemplate;
	} 

	 @Override
	    public String uploadDocuments(CandidateDocumentDto candidateDocument) throws IOException {
	        logger.info(MessageConstants.UPLOADING_DOCUMENTS_INFO, candidateDocument.getCandidateId());
	        CandidateDocument document = new CandidateDocument();

	        try {
	            document.setPayslip(uploadFile(candidateDocument.getPayslip()));
	            document.setExperienceLetter(uploadFile(candidateDocument.getExperienceLetter()));
	            document.setDegreeCertificate(uploadFile(candidateDocument.getDegreeCertificate()));

	            if (candidateDocument.getRelievingLetter() != null) {
	                document.setRelievingLetter(uploadFile(candidateDocument.getRelievingLetter()));
	                document.setRelievingLetterStatus(MessageConstants.UPLOADED);
	            } else {
	                logger.warn(MessageConstants.RELIEVING_LETTER_MISSING_WARNING);
	            }

	            if (candidateDocument.getPassport() != null) {
	                document.setPassport(uploadFile(candidateDocument.getPassport()));
	                document.setPassportStatus(MessageConstants.UPLOADED);
	            } else {
	                logger.warn(MessageConstants.PASSPORT_MISSING_WARNING);
	            }

	            document.setAadhar(uploadFile(candidateDocument.getAadhar()));
	            document.setPanCard(uploadFile(candidateDocument.getPanCard()));

	            if (candidateDocument.getPayslip() != null)
	                document.setPayslipStatus(MessageConstants.UPLOADED);
	            if (candidateDocument.getExperienceLetter() != null)
	                document.setExperienceLetterStatus(MessageConstants.UPLOADED);
	            if (candidateDocument.getDegreeCertificate() != null)
	                document.setDegreeCertificateStatus(MessageConstants.UPLOADED);
	            if (candidateDocument.getAadhar() != null)
	                document.setAadharStatus(MessageConstants.UPLOADED);
	            if (candidateDocument.getPanCard() != null)
	                document.setPanCardStatus(MessageConstants.UPLOADED);

	            CandidateDocument savedDocument = candidateDocumentDao.saveDocuments(document);

	            Candidate candidate = candidateDocumentDao.findById(candidateDocument.getCandidateId());
	            candidate.setDocuments(savedDocument);
	            candidateDocumentDao.update(candidate);

	            logger.info(MessageConstants.DOCUMENT_UPLOAD_SUCCESS, candidateDocument.getCandidateId());
	            return MessageConstants.DOCUMENT_UPLOAD_SUCCESS+ candidateDocument.getCandidateId() ;

	        } catch (IllegalArgumentException e) {
	            throw new DocumentUploadException(MessageConstants.INVALID_ARGUMENT_ERROR, e);
	        } catch (DataAccessException e) {
	            throw new DocumentUploadException(MessageConstants.DATA_ACCESS_ERROR, e);
	        } catch (Exception e) {
	            throw new DocumentUploadException(MessageConstants.GENERIC_UPLOAD_ERROR, e);
	        }
	    }

	    public String uploadFile(MultipartFile file) throws IOException {
	        logger.info(MessageConstants.FILE_UPLOAD_INFO);

	        if (file.isEmpty()) {
	            logger.warn(MessageConstants.FILE_NULL_OR_EMPTY_WARNING);
	            return null;
	        }

	        try {
	            return gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType())
	                    .toString();
	        } catch (Exception e) {
	            throw new DocumentUploadException(MessageConstants.FILE_UPLOAD_ERROR.replace("{}", file.getOriginalFilename()), e);
	        }
	    }
	@Override
	public CandidateDocumentDto getCandidateDocumentByCandidateId(Long candidateId) {
		logger.info(MessageConstants.FETCHING_DOCUMENTS, candidateId);
		CandidateDocument candidateDocument = candidateDocumentDao.getCandidateDocumentByCandidateId(candidateId);

		if (candidateDocument == null) {
			logger.error(MessageConstants.DOCUMENT_NOT_FOUND, candidateId);
			throw new CandidateDocumentNotFoundException(MessageConstants.DOCUMENT_NOT_FOUND + candidateId);
		}

		CandidateDocumentDto candidateDocumentDto = new CandidateDocumentDto();
		candidateDocumentDto.setAadharStatus(candidateDocument.getAadharStatus());
		candidateDocumentDto.setDegreeCertificateStatus(candidateDocument.getDegreeCertificateStatus());
		candidateDocumentDto.setExperienceLetterStatus(candidateDocument.getExperienceLetterStatus());
		candidateDocumentDto.setPanCardStatus(candidateDocument.getPanCardStatus());
		candidateDocumentDto.setPassportStatus(candidateDocument.getPassportStatus()); 
		candidateDocumentDto.setPayslipStatus(candidateDocument.getPayslipStatus());
		candidateDocumentDto.setRelievingLetterStatus(candidateDocument.getRelievingLetterStatus());

		Map<String, ResponseEntity<byte[]>> documentResponses = fetchDocuments(candidateDocument);
		candidateDocumentDto.setDocuments(documentResponses);

		logger.info(MessageConstants.DOCUMENT_RETRIEVED_SUCCESSFULLY, candidateId);
		return candidateDocumentDto;
	}

	public Map<String, ResponseEntity<byte[]>> fetchDocuments(CandidateDocument candidateDocument) {
		logger.info(MessageConstants.FETCHING_DOCUMENTS, candidateDocument.getCandidateDocumentId());
		Map<String, ResponseEntity<byte[]>> result = new HashMap<>();

		result.put("payslip", createResponseEntity(downloadFile(candidateDocument.getPayslip())));
		result.put("experienceLetter", createResponseEntity(downloadFile(candidateDocument.getExperienceLetter())));
		result.put("degreeCertificate", createResponseEntity(downloadFile(candidateDocument.getDegreeCertificate())));
		result.put("aadhar", createResponseEntity(downloadFile(candidateDocument.getAadhar())));
		result.put("panCard", createResponseEntity(downloadFile(candidateDocument.getPanCard())));
		result.put("passport", createResponseEntity(downloadFile(candidateDocument.getPassport())));
		result.put("relievingLetter", createResponseEntity(downloadFile(candidateDocument.getRelievingLetter())));

		logger.info(MessageConstants.DOCUMENTS_FETCHED_SUCCESSFULLY, candidateDocument.getCandidateDocumentId());
		return result;
	}

	private ResponseEntity<byte[]> createResponseEntity(CandidateDocumentDto candidateDocumentDto) {
		if (candidateDocumentDto != null) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + candidateDocumentDto.getOriginalFilename() + "\"")
					.body(candidateDocumentDto.getData());
		} else {
			logger.warn(MessageConstants.CANDIDATEDOCUMENT_DTO_NULL);
			return ResponseEntity.notFound().build();
		}
	}

	public CandidateDocumentDto downloadFile(String fileId) {
		logger.info(MessageConstants.DOWNLOADING_FILE, fileId);
		ObjectId objectId = new ObjectId(fileId);
		Query query = new Query(Criteria.where("_id").is(objectId));
		GridFsResource resource = gridFsTemplate.getResource(gridFsTemplate.findOne(query));

		if (!resource.exists()) {
			logger.error(MessageConstants.FILE_NOT_FOUND, fileId);
			throw new FileNotFoundException(MessageConstants.FILE_NOT_FOUND + fileId);
		}

		try (InputStream inputStream = resource.getInputStream()) {
			byte[] data = inputStream.readAllBytes();
			String originalFilename = resource.getFilename();
			String contentType = resource.getContentType() != null && !resource.getContentType().isEmpty() 
                    ? resource.getContentType() 
                    : "application/octet-stream";
			CandidateDocumentDto candidateDocumentDto = new CandidateDocumentDto();
			candidateDocumentDto.setData(data);
			candidateDocumentDto.setOriginalFilename(originalFilename);
			candidateDocumentDto.setContentType(contentType);

			logger.info(MessageConstants.DOWNLOADED_SUCCESSFULLY, originalFilename);
			return candidateDocumentDto;
		} catch (IOException e) {
			throw new DocumentUploadException(MessageConstants.ERROR_DOWNLOADING_FILE + fileId, e);
		}
	}

	@Override
	public String updateCandidateDocuments(CandidateDocumentDto candidateDocumentDto) {
		logger.info(MessageConstants.UPDATING_DOCUMENTS, candidateDocumentDto.getCandidateId());
		CandidateDocument existingDocument = candidateDocumentDao
				.getCandidateDocumentByCandidateId(candidateDocumentDto.getCandidateId());

		if (existingDocument == null) {
			logger.warn(MessageConstants.DOCUMENT_NOT_FOUND, candidateDocumentDto.getCandidateId());
			throw new CandidateDocumentNotFoundException(
					MessageConstants.DOCUMENT_NOT_FOUND + candidateDocumentDto.getCandidateId());
		}

		try {
			if (candidateDocumentDto.getPayslip() != null) {
				existingDocument.setPayslip(uploadFile(candidateDocumentDto.getPayslip()));
				existingDocument.setPayslipStatus(MessageConstants.REUPLOADED);
			}

			if (candidateDocumentDto.getExperienceLetter() != null) {
				existingDocument.setExperienceLetter(uploadFile(candidateDocumentDto.getExperienceLetter()));
				existingDocument.setExperienceLetterStatus(MessageConstants.REUPLOADED);
			}

			if (candidateDocumentDto.getDegreeCertificate() != null) {
				existingDocument.setDegreeCertificate(uploadFile(candidateDocumentDto.getDegreeCertificate()));
				existingDocument.setDegreeCertificateStatus(MessageConstants.REUPLOADED);
			}

			if (candidateDocumentDto.getAadhar() != null) {
				existingDocument.setAadhar(uploadFile(candidateDocumentDto.getAadhar()));
				existingDocument.setAadharStatus(MessageConstants.REUPLOADED);
			}

			if (candidateDocumentDto.getPanCard() != null) {
				existingDocument.setPanCard(uploadFile(candidateDocumentDto.getPanCard()));
				existingDocument.setPanCardStatus(MessageConstants.REUPLOADED);
			}

			if (candidateDocumentDto.getRelievingLetter() != null) {
				existingDocument.setRelievingLetter(uploadFile(candidateDocumentDto.getRelievingLetter()));
				existingDocument.setRelievingLetterStatus(MessageConstants.REUPLOADED);
			}

			if (candidateDocumentDto.getPassport() != null) {
				existingDocument.setPassport(uploadFile(candidateDocumentDto.getPassport()));
				existingDocument.setPassportStatus(MessageConstants.REUPLOADED);
			}

			CandidateDocument updatedDocument = candidateDocumentDao.updateCandidateDocuments(existingDocument);
			Candidate candidate = candidateDocumentDao.findById(candidateDocumentDto.getCandidateId());
			candidate.setDocuments(updatedDocument);
			candidateDocumentDao.update(candidate);

			logger.info(MessageConstants.DOCUMENTS_UPDATED_SUCCESSFULLY, candidateDocumentDto.getCandidateId());
			return MessageConstants.DOCUMENTS_UPDATED_SUCCESSFULLY+ candidateDocumentDto.getCandidateId();
		} catch (IOException e) {
			throw new DocumentUploadException(MessageConstants.ERROR_UPDATING_DOCUMENTS, e);
		}
	}

}