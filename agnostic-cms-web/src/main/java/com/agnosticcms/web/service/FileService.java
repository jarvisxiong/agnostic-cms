package com.agnosticcms.web.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.agnosticcms.web.dto.ColumnType;
import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.dto.form.ModuleInput;
import com.agnosticcms.web.exception.ServiceRuntimeException;

@Service
public class FileService {
	
	public static final String IMG_DIR = "images";

	@Value("${path.uploads}")
	private String uploadsPath;
	
	public void saveImages(Module module, List<ModuleColumn> moduleColumns, ModuleInput moduleInput) {
		
		Map<Long, MultipartFile> files = moduleInput.getFiles();
		Map<Long, String> columnValues = moduleInput.getColumnValues();
		
		for (ModuleColumn moduleColumn : moduleColumns) {
			if(moduleColumn.getType() == ColumnType.IMAGE) {
				Long columnId = moduleColumn.getId();
				MultipartFile image = files.get(columnId);
				
				if(image != null && !image.isEmpty()) {
					String imagePath = saveImage(module, image);
					columnValues.put(columnId, imagePath);
				}
			}
		}
		
	}
	
	private String saveImage(Module module, MultipartFile image) {
		
		String relativeDestinationFolder = getDestinationFolder(IMG_DIR, module.getTableName());
		String absoluteDestinationFolder = uploadsPath + relativeDestinationFolder;
		String cleanedFileName = image.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
		String finalFileName = cleanedFileName;
		
		File file = new File(absoluteDestinationFolder + finalFileName);
		while (file.exists()) {
			finalFileName = randomizeFilename(cleanedFileName);
			file = new File(absoluteDestinationFolder + finalFileName);
		}
		
		file.getParentFile().mkdirs();
		
		try {
			FileUtils.writeByteArrayToFile(file, image.getBytes());
		} catch (IOException e) {
			throw new ServiceRuntimeException("Unable to save uploded image", e);
		}
		
		return relativeDestinationFolder + finalFileName;
		
	}
	
	private String randomizeFilename(String oldFilename) {
		Random random = new Random();
		String baseName = FilenameUtils.getBaseName(oldFilename);
		String ext = FilenameUtils.getExtension(oldFilename);
		
		StringBuilder sb = new StringBuilder();
		sb.append(baseName);
		sb.append("_");
		sb.append(Math.abs(random.nextLong()));
		sb.append(".");
		sb.append(ext);
		return sb.toString();
	}
	
	private String getDestinationFolder(String fileDir, String moduleName) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("/");
		sb.append(fileDir);
		sb.append("/");
		sb.append(moduleName);
		sb.append("/");
		return sb.toString();
		
	}
	
}
