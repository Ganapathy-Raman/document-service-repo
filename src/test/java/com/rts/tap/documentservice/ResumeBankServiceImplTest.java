package com.rts.tap.documentservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mock.web.MockMultipartFile;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.rts.tap.dao.ResumeBankMongoDao;
import com.rts.tap.exception.ResumeNotFoundException;
import com.rts.tap.exception.ResumeSaveException;
import com.rts.tap.model.ResumeBank;
import com.rts.tap.serviceimplementation.ResumeBankServiceImpl;

@ExtendWith(MockitoExtension.class)
class ResumeBankServiceImplTest {

	@Mock
	private GridFsTemplate gridFsTemplate;

	@Mock
	private ResumeBankMongoDao resumeBankMongoDao;

	@InjectMocks
	private ResumeBankServiceImpl resumeBankServiceImpl;

	private MockMultipartFile mockFile;
	private String testFileId;
	private byte[] content;

	@BeforeEach
	void setUp() {
		content = "Test content".getBytes();
		mockFile = new MockMultipartFile("testfile", "testfile.pdf", "application/pdf", content);
		testFileId = new ObjectId().toString();
	}

	@Test
	void testAddCandidateResume() throws IOException {
		ResumeBank mockResumeBank = mock(ResumeBank.class);
		when(gridFsTemplate.store(any(), anyString(), anyString())).thenReturn(new ObjectId(testFileId));
		when(resumeBankMongoDao.save(any())).thenReturn(mockResumeBank);
		when(mockResumeBank.getId()).thenReturn(testFileId);

		String resumeId = resumeBankServiceImpl.addcandidateResume(mockFile);

		verify(resumeBankMongoDao).save(any(ResumeBank.class));
		assertEquals(resumeId, testFileId);
	}

	@Test
	void testGetAllResumeBank() {
		ResumeBank resume = new ResumeBank();
		resume.setFileId(testFileId);
		resume.setFileName("testfile.pdf");

		when(resumeBankMongoDao.findAll()).thenReturn(Collections.singletonList(resume));

		List<ResumeBank> resumes = resumeBankServiceImpl.getAllResumeBank();

		assertEquals(1, resumes.size());
		assertEquals("testfile.pdf", resumes.get(0).getFileName());
	}

	@Test
	void testUpdateResumeById() throws IOException {
		ResumeBank resume = new ResumeBank();
		resume.setFileId(testFileId);
		resume.setFileName("testfile.pdf");

		when(resumeBankMongoDao.findById(testFileId)).thenReturn(Optional.of(resume));
		when(gridFsTemplate.store(any(), anyString(), anyString())).thenReturn(new ObjectId(testFileId));
		when(resumeBankMongoDao.save(any())).thenReturn(resume);

		resumeBankServiceImpl.updateResumeById(testFileId, mockFile);
		verify(resumeBankMongoDao).save(resume);
	}

	@Test
	void testUpdateResumeByIdResumeNotFound() {
		when(resumeBankMongoDao.findById(testFileId)).thenReturn(Optional.empty());

		assertThrows(ResumeNotFoundException.class, () -> {
			resumeBankServiceImpl.updateResumeById(testFileId, mockFile);
		});
	}

	@Test
	void testUpdateResumeByIdIOException() throws IOException {
		ResumeBank resume = new ResumeBank();
		resume.setFileId(testFileId);
		resume.setFileName("testfile.pdf");
	}

	@Test
	void testGetResumeById() throws IOException {
		ResumeBank resume = new ResumeBank();
		resume.setFileId(testFileId);
		resume.setFileName("testfile.pdf");
	}

	@Test
	void testGetResumeById_NotFound() {
		when(resumeBankMongoDao.findById(testFileId)).thenReturn(Optional.empty());

		assertThrows(ResumeNotFoundException.class, () -> {
			resumeBankServiceImpl.getResumeById(testFileId);
		});
	}

	@Test
	void testGetResumeById_GridFSNotFound() throws IOException {
		ResumeBank resume = new ResumeBank();
		resume.setFileId(testFileId);
		resume.setFileName("testfile.pdf");
		when(resumeBankMongoDao.findById(testFileId)).thenReturn(Optional.of(resume));

		GridFSFindIterable mockGridFSFindIterable = mock(GridFSFindIterable.class);
		when(gridFsTemplate.find(any())).thenReturn(mockGridFSFindIterable);

		when(mockGridFSFindIterable.first()).thenReturn(null);

		assertThrows(ResumeNotFoundException.class, () -> {
			resumeBankServiceImpl.getResumeById(testFileId);
		});
	}

	@Test
	void testSaveResume() {
		ResumeBank resumeBank = new ResumeBank();
		resumeBank.setFileId(testFileId);
		resumeBank.setFileName("testfile.pdf");

		when(resumeBankMongoDao.save(any())).thenReturn(resumeBank);
		resumeBankServiceImpl.saveResume(resumeBank);

		verify(resumeBankMongoDao).save(resumeBank);
	}

	@Test
	void testSaveResumeException() {
		ResumeBank resumeBank = new ResumeBank();
		resumeBank.setFileId(testFileId);
		resumeBank.setFileName("testfile.pdf");

		when(resumeBankMongoDao.save(any())).thenThrow(new RuntimeException("DB Error"));
		assertThrows(ResumeSaveException.class, () -> resumeBankServiceImpl.saveResume(resumeBank));
	}
}