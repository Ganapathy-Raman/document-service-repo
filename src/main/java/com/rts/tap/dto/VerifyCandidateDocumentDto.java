package com.rts.tap.dto;
 
public class VerifyCandidateDocumentDto {
 
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
	
}
 
 