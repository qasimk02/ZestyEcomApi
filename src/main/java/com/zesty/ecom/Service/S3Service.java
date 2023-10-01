package com.zesty.ecom.Service;

public interface S3Service {
	
	String uploadToS3(byte[] fileBytes, String key);
	
}
