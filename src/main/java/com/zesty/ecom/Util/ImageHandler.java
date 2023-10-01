package com.zesty.ecom.Util;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Slf4j
@Service
public class ImageHandler {
	
	public HashMap<String,byte[]> handleProductImage(MultipartFile file,String fileName){
		HashMap<String,byte[]> map = new HashMap<>();
		
		String baseName = fileName.substring(0, fileName.lastIndexOf("."));
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

		// Small image
		String smallImageName = baseName + "-small." + extension;
		byte[] smallImage = resizeImageToBytes(file, 300, (1.33), 0.8);
		String mediumImageName = baseName + "-medium." + extension;
		byte[] mediumImage = resizeImageToBytes(file, 700, (1.33), 0.8);
		String largeImageName = baseName + "-large." + extension;
		byte[] largeImage = resizeImageToBytes(file, 1200, (1.33), 0.8);
		
		
		map.put(smallImageName,smallImage);
		map.put(mediumImageName,mediumImage);
		map.put(largeImageName, largeImage);
		return map;
	}
	
	private byte[] resizeImageToBytes(MultipartFile file, int width, double ratio, double quality) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			Thumbnails.of(file.getInputStream()).forceSize(width, (int)(width*ratio)).outputQuality(quality)
					.toOutputStream(outputStream);
		} catch (Exception ex) {
			log.error("Error resizing the image", ex);
		}
		return outputStream.toByteArray();
	}
	
}
