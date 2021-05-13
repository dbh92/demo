package com.example.demo2.service.impl;

import com.example.demo2.exception.FileStorageException;
import com.example.demo2.property.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public List<String> uploadFileService(MultipartFile[] files) {
        List<String> fileNames = Arrays.asList(files)
                .stream()
                .map(file -> {
                    try {
                        Files.copy(file.getInputStream(), this.fileStorageLocation.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return file.getOriginalFilename();
                })
                .collect(Collectors.toList());
        return fileNames;
    }

//    public void store(MultipartFile file) {
//        try {
//            Files.copy(file.getInputStream(), this.fileStorageLocation.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
//        } catch (Exception e) {
//            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
//        }
//    }

    public Resource loadFile(String filename) {
        try {
            Path file = fileStorageLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error! -> message = " + e.getMessage());
        }
    }

    public List<Path> loadFiles() {
        List<Path> result = null;
        try (Stream<Path> walk = Files.walk(this.fileStorageLocation, 1)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

