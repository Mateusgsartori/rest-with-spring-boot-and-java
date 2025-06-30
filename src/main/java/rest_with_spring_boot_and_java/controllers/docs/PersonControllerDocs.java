package rest_with_spring_boot_and_java.controllers.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rest_with_spring_boot_and_java.data.dto.PersonDTO;

import java.util.List;

public interface PersonControllerDocs {
    @Operation(summary = "Finds a person",
            description = "Finds a person by it's id",
            tags = {"People"},
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
    PersonDTO findById(@PathVariable("id") Long id);

    @Operation(summary = "Finds all people",
            description = "Finds all people",
            tags = {"People"},
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
                                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                                    )
                            })
            })
    List<PersonDTO> findAll();

    @Operation(summary = "creates a person",
            description = "creates a new person",
            tags = {"People"},
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
    PersonDTO createPerson(@RequestBody PersonDTO person);

        @Operation(summary = "Updates a person",
                description = "Updates a person by it's id",
                tags = {"People"},
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
    PersonDTO updatePerson(@RequestBody PersonDTO person);

    @Operation(summary = "Deletes a person",
            description = "Deletes a person by it's id",
            tags = {"People"},
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
    ResponseEntity<?> deletePerson(@PathVariable("id") Long id);
}
