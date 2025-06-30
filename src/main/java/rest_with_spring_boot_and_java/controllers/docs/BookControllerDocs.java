package rest_with_spring_boot_and_java.controllers.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import rest_with_spring_boot_and_java.data.dto.BookDTO;
import rest_with_spring_boot_and_java.data.dto.PersonDTO;

import java.util.List;

public interface BookControllerDocs {
    @Operation(summary = "Finds a book",
            description = "Finds a book by it's id",
            tags = {"Books"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Success",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = BookDTO.class)))
            })
    BookDTO findById(@PathVariable("id") Long id);

    @Operation(summary = "Finds all books",
            description = "Finds all books",
            tags = {"Books"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Success",
                            responseCode = "404",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))
                                    )
                            })
            })
    List<BookDTO> findAll();

    @Operation(summary = "creates a book",
            description = "creates a new book",
            tags = {"Books"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Success",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = BookDTO.class)))
            })
    BookDTO createBook(@RequestBody BookDTO person);

        @Operation(summary = "Updates a book",
                description = "Updates a book by it's id",
                tags = {"Books"},
                responses = {
                        @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                        @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
                        @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                        @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                        @ApiResponse(description = "Success",
                                responseCode = "404",
                                content = @Content(schema = @Schema(implementation = BookDTO.class)))
                })
    BookDTO updateBook(@RequestBody BookDTO person);

    @Operation(summary = "Deletes a book",
            description = "Deletes a book by it's id",
            tags = {"Books"},
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
    ResponseEntity<?> deleteBook(@PathVariable("id") Long id);
}
