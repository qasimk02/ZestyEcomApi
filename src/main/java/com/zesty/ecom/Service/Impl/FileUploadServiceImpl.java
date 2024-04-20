package com.zesty.ecom.Service.Impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.zesty.ecom.Service.FileUploadService;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Service
public class FileUploadServiceImpl implements FileUploadService{
	
//	@Value("${application.bucket.name}")
//	private String bucketName;
//
//	@Autowired
//	private S3Client s3Client;
	
	@Autowired
	private Cloudinary cloudinary;

	// upload to s3
//	public String uploadToS3(byte[] fileBytes, String key) {
//		PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(bucketName).key(key).build();
//		s3Client.putObject(objectRequest, RequestBody.fromBytes(fileBytes));
//		return "File Uploaded Succesfully at " + key;
//	}
	
	
	public Map<?, ?> uploadtoCloudinary(byte[] image, String publicId) throws IOException {
        Map<?,?> map = this.cloudinary.uploader().upload(image, ObjectUtils.asMap("public_id", publicId));
        return map;
	}

	@Override
	public String uploadToS3(byte[] fileBytes, String key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
