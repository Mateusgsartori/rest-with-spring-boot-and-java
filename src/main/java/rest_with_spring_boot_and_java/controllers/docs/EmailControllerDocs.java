package rest_with_spring_boot_and_java.controllers.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import rest_with_spring_boot_and_java.data.dto.request.EmailRequestDTO;

public interface EmailControllerDocs {


    @Operation(summary = "Sends an e-mail",
            description = "Sends an e-mail by providing details, subject and body",
            tags = {"E-mail"},
            responses = {
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Success", responseCode = "200",content = @Content)
            })
    ResponseEntity<String> sendEmail(EmailRequestDTO emailRequestDTO);

    @Operation(summary = "Sends an e-mail with attachment",
            description = "Sends an e-mail with attachment by providing details, subject and body",
            tags = {"E-mail"},
            responses = {
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Success", responseCode = "200",content = @Content)
            })
    ResponseEntity<String> sendEmailWithAttachment(String emailRequestJson, MultipartFile multipartFile);

}
