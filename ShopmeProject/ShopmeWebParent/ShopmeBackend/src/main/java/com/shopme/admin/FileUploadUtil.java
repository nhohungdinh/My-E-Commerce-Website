package com.shopme.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
	
		Path uploadPath = Paths.get(uploadDir); // IDE se tao uploadPath = defaultDirectory (ShopmeBackend) + uploadDir
		if (!Files.exists(uploadPath)) {
			// neu chua co thu muc uploadPath thi tao moi
			Files.createDirectories(uploadPath);	
		}
		
		try (InputStream inputStream = multipartFile.getInputStream()) {
			// try with resource - cac resource trong dau () se bi close sau khi thuc hien xong, giong nhu finally block
			// multipartFile.getInputStream() - multipartFile se tra ve duoi dang stream byte, dung de doc du lieu file sang dang byte
			Path filePath = uploadPath.resolve(fileName); // filePath = uploadPath\fileName
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new IOException("Could not save file: " + fileName, e);
		}
	}
	
	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);
		try {
			Files.list(dirPath).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					}
					catch (IOException e) {
						System.out.println("Could not delete file: " + file);
					}
				}
			});
		} catch (IOException e) {
			System.out.println("Could not list directory: " + dirPath);
		}
	}
	
	
}
