package rest_with_spring_boot_and_java.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import rest_with_spring_boot_and_java.config.FileStorageConfig;
import rest_with_spring_boot_and_java.handler.FileNotFoundException;
import rest_with_spring_boot_and_java.handler.FileStorageException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
        this.fileStorageLocation = path;
        try {
            log.info("Creating directory");
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new FileStorageException("Could not create directory where files will be stored!", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                log.error("Sorry, file name contains a invalid path sequence {}", fileName);
                throw new FileStorageException("Sorry, file name contains a invalid path sequence " + fileName);
            }
            log.info("Saving file");
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new FileStorageException("Could not store file " + fileName + ". Please try again", e);
        }

    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new FileNotFoundException("File not found " + fileName);
            }
            return resource;
        } catch (Exception e) {
            log.error("File not found");
            throw new FileNotFoundException("File not found " + fileName, e);
        }
    }
}
