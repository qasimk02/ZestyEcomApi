package com.zesty.ecom.Service;

import java.io.IOException;
import java.util.Map;

public interface FileUploadService {
	
	String uploadToS3(byte[] fileBytes, String key);

	Map<?, ?> uploadtoCloudinary(byte[] image, String publicId) throws IOException;
	
}
