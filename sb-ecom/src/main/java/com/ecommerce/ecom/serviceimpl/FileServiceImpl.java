package com.ecommerce.ecom.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.ecom.service.FileService;

@Service
public class FileServiceImpl implements FileService{
	
	@Override
	public String uploadImage(String path,MultipartFile image) throws IOException {
		/* get the file name i.e. image name which is current/original file */
		String originalName = image.getOriginalFilename();
		
		/* Generate unique file name */
		String randomIdforImage = UUID.randomUUID().toString();
		
		/* if image name demo.jpg then below method transform it into 1234.jpg*/
		String fileNameforImage = randomIdforImage.concat(originalName.substring(originalName.lastIndexOf('.')));
		
		/*this will concate file path with image */
		String filePath = path + File.separator+fileNameforImage;
		
		/*Check if path exist or create*/
		
		File folder = new File(path);
		
		if(!folder.exists())
			folder.mkdir();
		
		/*this will copy the inputstream to the path we have specified*/
		Files.copy(image.getInputStream(), Paths.get(filePath));
		
		return fileNameforImage;
	}

}
