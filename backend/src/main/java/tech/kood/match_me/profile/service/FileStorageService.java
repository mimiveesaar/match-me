package tech.kood.match_me.profile.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;              
import java.nio.file.Path;               
import java.nio.file.Paths;              
import java.nio.file.StandardCopyOption; 
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:${user.home}/profile-images}")
    private String uploadDir;

    public String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return filename;
    }

    public Resource loadFile(String filename) throws IOException {
        if (filename == null || filename.isEmpty() || filename.startsWith("http")) {
            return getDefaultProfileImage();
        }

        Path imagePath = Paths.get(uploadDir).resolve(filename);
        Resource resource = new UrlResource(imagePath.toUri());
        
        return (resource.exists() && resource.isReadable()) ? resource : getDefaultProfileImage();
    }

    public void deleteFile(String filename) throws IOException {
        if (filename == null || filename.isEmpty() || filename.startsWith("http")) {
            return;
        }
        
        Path imagePath = Paths.get(uploadDir).resolve(filename);
        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
        }
    }

    private Resource getDefaultProfileImage() throws IOException {
        ClassPathResource defaultImage = new ClassPathResource("static/images/default-profile.png");
        if (defaultImage.exists()) {
            return defaultImage;
        }
        throw new FileNotFoundException("Default profile image not found");
    }
}
