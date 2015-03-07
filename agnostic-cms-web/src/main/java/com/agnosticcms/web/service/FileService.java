package com.agnosticcms.web.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.exception.ServiceRuntimeException;

@Service
public class FileService {
	
	public static final String IMG_DIR = "images";

	@Value("${path.uploads}")
	private String uploadsPath;
	
	public void saveImage(Module module, MultipartFile image) {
		
		String destinationFolder = getDestinationFolder(IMG_DIR, module.getName());
		String finalFileName = image.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
		
		File file = new File(destinationFolder + finalFileName);
		
		file.mkdirs();
		
		try {
			FileUtils.writeByteArrayToFile(file, image.getBytes());
		} catch (IOException e) {
			throw new ServiceRuntimeException("Unable to save uploded image", e);
		}
		
	}
	
	private String getDestinationFolder(String fileDir, String moduleName) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(uploadsPath);
		sb.append("/");
		sb.append(fileDir);
		sb.append("/");
		sb.append(moduleName);
		sb.append("/");
		return sb.toString();
		
	}
	
}
