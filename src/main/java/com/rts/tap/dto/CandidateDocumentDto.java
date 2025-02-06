package com.rts.tap.dto;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class CandidateDocumentDto {

	private Long documentId;

	private Long candidateId;

	private MultipartFile payslip;

	private MultipartFile experienceLetter;

	private MultipartFile degreeCertificate;

	private MultipartFile aadhar;

	private MultipartFile panCard;

	private MultipartFile passport;

	private MultipartFile relievingLetter;

	private String payslipStatus;

	private String experienceLetterStatus;

	private String degreeCertificateStatus;

	private String aadharStatus;

	private String panCardStatus;

	private String passportStatus;

	private String relievingLetterStatus;

	private byte[] data;
	private String originalFilename;
	private String contentType;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Map<String, ResponseEntity<byte[]>> getDocuments() {
		return documents;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

	public MultipartFile getPayslip() {
		return payslip;
	}

	public void setPayslip(MultipartFile payslip) {
		this.payslip = payslip;
	}

	public MultipartFile getExperienceLetter() {
		return experienceLetter;
	}

	public void setExperienceLetter(MultipartFile experienceLetter) {
		this.experienceLetter = experienceLetter;
	}

	public MultipartFile getDegreeCertificate() {
		return degreeCertificate;
	}

	public void setDegreeCertificate(MultipartFile degreeCertificate) {
		this.degreeCertificate = degreeCertificate;
	}

	public MultipartFile getAadhar() {
		return aadhar;
	}

	public void setAadhar(MultipartFile aadhar) {
		this.aadhar = aadhar;
	}

	public MultipartFile getPanCard() {
		return panCard;
	}

	public void setPanCard(MultipartFile panCard) {
		this.panCard = panCard;
	}

	public MultipartFile getPassport() {
		return passport;
	}

	public void setPassport(MultipartFile passport) {
		this.passport = passport;
	}

	public MultipartFile getRelievingLetter() {
		return relievingLetter;
	}

	public void setRelievingLetter(MultipartFile relievingLetter) {
		this.relievingLetter = relievingLetter;
	}

	public String getPayslipStatus() {
		return payslipStatus;
	}

	public void setPayslipStatus(String payslipStatus) {
		this.payslipStatus = payslipStatus;
	}

	public String getExperienceLetterStatus() {
		return experienceLetterStatus;
	}

	public void setExperienceLetterStatus(String experienceLetterStatus) {
		this.experienceLetterStatus = experienceLetterStatus;
	}

	public String getDegreeCertificateStatus() {
		return degreeCertificateStatus;
	}

	public void setDegreeCertificateStatus(String degreeCertificateStatus) {
		this.degreeCertificateStatus = degreeCertificateStatus;
	}

	public String getAadharStatus() {
		return aadharStatus;
	}

	public void setAadharStatus(String aadharStatus) {
		this.aadharStatus = aadharStatus;
	}

	public String getPanCardStatus() {
		return panCardStatus;
	}

	public void setPanCardStatus(String panCardStatus) {
		this.panCardStatus = panCardStatus;
	}

	public String getPassportStatus() {
		return passportStatus;
	}

	public void setPassportStatus(String passportStatus) {
		this.passportStatus = passportStatus;
	}

	public String getRelievingLetterStatus() {
		return relievingLetterStatus;
	}

	public void setRelievingLetterStatus(String relievingLetterStatus) {
		this.relievingLetterStatus = relievingLetterStatus;
	}

	private Map<String, ResponseEntity<byte[]>> documents;

	public void setDocuments(Map<String, ResponseEntity<byte[]>> documents) {
		this.documents = documents;
	}

}
