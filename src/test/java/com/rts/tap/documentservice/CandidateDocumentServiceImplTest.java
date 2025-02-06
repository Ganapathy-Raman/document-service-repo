package com.rts.tap.documentservice;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.rts.tap.dao.CandidateDocumentDao;
import com.rts.tap.dto.CandidateDocumentDto;
import com.rts.tap.model.Candidate;
import com.rts.tap.model.CandidateDocument;
import com.rts.tap.serviceimplementation.CandidateDocumentServiceImpl;

class CandidateDocumentServiceImplTest {
 
    @Mock
    private CandidateDocumentDao candidateDocumentDao;

    @Mock
    private GridFsTemplate gridFsTemplate;

    @InjectMocks
    private CandidateDocumentServiceImpl candidateDocumentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private CandidateDocumentDto mockCandidateDocumentDto() {
        CandidateDocumentDto documentDto = new CandidateDocumentDto();
        documentDto.setCandidateId(1L);
        
        documentDto.setPayslip(mockFile("payslip.pdf"));
        documentDto.setExperienceLetter(mockFile("experience_letter.pdf"));
        documentDto.setDegreeCertificate(mockFile("degree_certificate.pdf"));
        documentDto.setAadhar(mockFile("aadhar.pdf"));
        documentDto.setPanCard(mockFile("pan_card.pdf"));
        documentDto.setRelievingLetter(mockFile("relieving_letter.pdf"));
        documentDto.setPassport(mockFile("passport.pdf"));
        
        return documentDto;
    }

    private MultipartFile mockFile(String filename) {
        return new MultipartFile() {
            private final String originalFilename = filename;

            @Override
            public String getName() {
                return originalFilename;
            }

            @Override
            public String getOriginalFilename() {
                return originalFilename;
            }

            @Override
            public String getContentType() {
                return "application/pdf";
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 1000; 
            }

            @Override
            public byte[] getBytes() {
                return new byte[1000]; 
            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(new byte[1000]);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException { }
        };
    }

    
    @Test
    void testUploadDocuments() throws IOException {
        CandidateDocumentDto documentDto = mockCandidateDocumentDto();
        CandidateDocument savedDocument = new CandidateDocument();
        Candidate candidate = new Candidate();

        when(gridFsTemplate.store(any(), anyString(), anyString())).thenReturn(new ObjectId());
        when(candidateDocumentDao.saveDocuments(any())).thenReturn(savedDocument);
        when(candidateDocumentDao.findById(documentDto.getCandidateId())).thenReturn(candidate);
        
        candidateDocumentService.uploadDocuments(documentDto);
        
        verify(candidateDocumentDao).update(candidate);
    }

    @Test
    void testUploadFile() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        
        when(mockFile.getOriginalFilename()).thenReturn("test.txt");
        when(mockFile.getSize()).thenReturn(1024L);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("test data".getBytes()));
        
        ObjectId fileId = new ObjectId();
        when(gridFsTemplate.store(any(), anyString(), anyString())).thenReturn(fileId);

    }

    @Test
    void testGetCandidateDocumentByCandidateId() {
        CandidateDocument candidateDocument = new CandidateDocument();
        candidateDocument.setCandidateDocumentId(1L);
        when(candidateDocumentDao.getCandidateDocumentByCandidateId(1L)).thenReturn(candidateDocument);
        when(candidateDocumentDao.getCandidateDocumentByCandidateId(anyLong())).thenReturn(candidateDocument);
    }

    @Test
    void testUpdateCandidateDocuments() throws IOException {
        CandidateDocumentDto documentDto = new CandidateDocumentDto();
        documentDto.setCandidateId(1L);     
        CandidateDocument existingDocument = new CandidateDocument();
        when(candidateDocumentDao.getCandidateDocumentByCandidateId(1L)).thenReturn(existingDocument);
        
    }

    @Test
    void testDownloadFile() throws IOException {
        ObjectId objectId = new ObjectId();

        GridFSFile mockGridFSFile = mock(GridFSFile.class);
        when(mockGridFSFile.getObjectId()).thenReturn(objectId); 

        Query query = new Query(Criteria.where("_id").is(objectId));
        when(gridFsTemplate.findOne(query)).thenReturn(mockGridFSFile);

        GridFsResource mockResource = mock(GridFsResource.class);
        
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.getInputStream()).thenReturn(new ByteArrayInputStream("test data".getBytes()));
        when(mockResource.getFilename()).thenReturn("file.txt");
        when(mockResource.getContentType()).thenReturn("application/pdf");

        when(gridFsTemplate.getResource(mockGridFSFile)).thenReturn(mockResource);

        CandidateDocumentDto result = candidateDocumentService.downloadFile(objectId.toHexString());
        
        assertNotNull(result);
        assertEquals("file.txt", result.getOriginalFilename());
        assertArrayEquals("test data".getBytes(), result.getData());
    }

}