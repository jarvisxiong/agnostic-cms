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

/**
 * Service for storing files from user form submission into file system
 */
@Service
public class FileService {
	
	/**
	 * Name of the directory for images
	 */
	public static final String IMG_DIR = "images";

	/**
	 * File system path for uploads
	 */
	@Value("${uploads.path}")
	private String uploadsPath;
	
	/**
	 * Store uploaded images onto file system and add file path to column values
	 * @param module The module of the submission
	 * @param moduleColumns Columns of the module
	 * @param moduleInput Submitted module input
	 */
	public void saveImages(Module module, List<ModuleColumn> moduleColumns, ModuleInput moduleInput) {
		
		Map<Long, MultipartFile> files = moduleInput.getFiles();
		Map<Long, String> columnValues = moduleInput.getColumnValues();
		
		for (ModuleColumn moduleColumn : moduleColumns) {
			// We have interest only in types of a file
			if(moduleColumn.getType() == ColumnType.IMAGE) {
				Long columnId = moduleColumn.getId();
				MultipartFile image = files.get(columnId);
				
				if(image != null && !image.isEmpty()) {
					// Store image and get relative image path back
					String imagePath = saveImage(module, image);
					// Put the path into database column
					columnValues.put(columnId, imagePath);
				}
			}
		}
		
	}
	
	/**
	 * Store a single image onto file system
	 * @param module Module for which the form was submitted
	 * @param image The image to store
	 * @return Relative image path
	 */
	private String saveImage(Module module, MultipartFile image) {
		// For each file goup (images, docs, etc.) a separate directory will be maintained.
		// These folders are then divided by module table names
		String relativeDestinationFolder = getDestinationFolder(IMG_DIR, module.getTableName());
		String absoluteDestinationFolder = uploadsPath + relativeDestinationFolder;
		// Removing characters that could create potential problems
		String cleanedFileName = image.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
		String finalFileName = cleanedFileName;
		
		File file = new File(absoluteDestinationFolder + finalFileName);
		// if randomized file name already exists (which is unlikely) - try harder
		while (file.exists()) {
			finalFileName = randomizeFilename(cleanedFileName);
			file = new File(absoluteDestinationFolder + finalFileName);
		}
		
		// Create missing directories if they do not exist yet
		file.getParentFile().mkdirs();
		
		try {
			// Store the image
			FileUtils.writeByteArrayToFile(file, image.getBytes());
		} catch (IOException e) {
			throw new ServiceRuntimeException("Unable to save uploded image", e);
		}
		
		return relativeDestinationFolder + finalFileName;
		
	}
	/**
	 * Add a random number to the given filename
	 * @param oldFilename The filename to add the number to
	 * @return Filename with the random number
	 */
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
	
	/**
	 * Creates relative destination path for file storage
	 * @param fileDir Base directory to put files into
	 * @param moduleName The name of the module, for which a separate directory will be maintained
	 * @return Relative file destination path
	 */
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
