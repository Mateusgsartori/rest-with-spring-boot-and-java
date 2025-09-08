package rest_with_spring_boot_and_java.controllers.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import rest_with_spring_boot_and_java.data.dto.PersonDTO;
import rest_with_spring_boot_and_java.data.dto.UploadFileResponseDTO;

import java.util.List;

@Tag(name = "Upload")
public interface FileControllerDocs {

    @Operation(summary = "Uploads a file",
            description = "Uploads a file",
            tags = {"Upload"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Success",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = PersonDTO.class)))
            })
    UploadFileResponseDTO uploadFile(MultipartFile file);

    @Operation(summary = "Uploads multiple files",
            description = "Allows uploading more than one file",
            tags = {"Upload"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Success",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = PersonDTO.class)))
            })
    List<UploadFileResponseDTO> uploadMultipleFiles(MultipartFile[] files);

    @Operation(summary = "Downloads a file",
            description = "Downloads a file",
            tags = {"Upload"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Success",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = PersonDTO.class)))
            })
    ResponseEntity<Resource> downloadFile(String fileName, HttpServletRequest request);

}
