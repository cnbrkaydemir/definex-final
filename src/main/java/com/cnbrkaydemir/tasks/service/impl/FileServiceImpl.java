package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.service.FileService;
import lombok.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {


    private final Path uploadLocation;

    @Value("${file.upload.dir}")
    private String uploadDir;

    public FileServiceImpl() {
        this.uploadLocation = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.uploadLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }

    }

    @Override
    public String store(MultipartFile file, UUID taskId) {
        Path taskDirectory = this.uploadLocation.resolve(taskId.toString());
        try {
            Files.createDirectories(taskDirectory);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create task directory.", ex);
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // Generate unique filename to prevent conflicts
        String fileExtension = "";
        if (originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        try {
            Path targetLocation = taskDirectory.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return uniqueFilename;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFilename +" !");
        }
    }

    @Override
    public Resource load(String fileName, UUID taskId) {
        try {
            Path filePath = this.uploadLocation.resolve(taskId.toString()).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }
}
