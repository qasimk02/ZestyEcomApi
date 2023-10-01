package com.zesty.ecom.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zesty.ecom.Service.S3Service;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Service
public class S3ServiceImpl implements S3Service{
	
	@Value("${application.bucket.name}")
	private String bucketName;

	@Autowired
	private S3Client s3Client;

	// upload to s3
	public String uploadToS3(byte[] fileBytes, String key) {
		PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(bucketName).key(key).build();
		s3Client.putObject(objectRequest, RequestBody.fromBytes(fileBytes));
		return "File Uploaded Succesfully at " + key;
	}
	
}
