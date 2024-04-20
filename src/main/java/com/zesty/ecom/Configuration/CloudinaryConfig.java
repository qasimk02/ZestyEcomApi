package com.zesty.ecom.Configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {
	
	@Value("${cloud.cloudinary.cloud-name}")
	private String cloudName;
	
	@Value("${cloud.cloudinary.credentials.api-key}")
	private String apiKey;
	
	@Value("${cloud.cloudinary.credentials.api-secret}")
	private String apiSecret;
	
	@Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = null;
        Map<String,String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }
	
}
