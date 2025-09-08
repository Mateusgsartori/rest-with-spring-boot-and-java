package rest_with_spring_boot_and_java.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rest_with_spring_boot_and_java.controllers.docs.FileControllerDocs;
import rest_with_spring_boot_and_java.data.dto.UploadFileResponseDTO;
import rest_with_spring_boot_and_java.service.FileStorageService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/file")
public class FileController implements FileControllerDocs {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService service;

    @PostMapping("/upload-file")
    @Override
    public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {
       String fileName = service.storeFile(file);
       String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
               .path("/api/file/download-file/")
               .path(fileName).toUriString();
       return new UploadFileResponseDTO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/upload-multiple-files")
    @Override
    public List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }

    @GetMapping("/download-file/{fileName:.+}")
    @Override
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = service.loadFileAsResource(fileName);

        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        } catch (Exception e) {
            log.error("Could not determine file type!");
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }
}
