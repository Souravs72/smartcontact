package com.smart.configuration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public String uploadImage(String path, MultipartFile multipartFile) throws IOException {
		//File name :
		String randomId = UUID.randomUUID().toString();
		String name = multipartFile.getOriginalFilename();
		String fileName = randomId.concat(name.substring(name.lastIndexOf(".")));
		
		
		
		//Create a full path to the file
		File saveImage = new ClassPathResource("static/images").getFile();
		Path newPath = Paths.get(saveImage.getAbsolutePath()+File.separator+fileName);
		Files.copy(multipartFile.getInputStream(), newPath, StandardCopyOption.REPLACE_EXISTING);
		
		
		return fileName;
	}
}
