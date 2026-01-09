package rest_with_spring_boot_and_java.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rest_with_spring_boot_and_java.controllers.docs.PersonControllerDocs;
import rest_with_spring_boot_and_java.data.dto.PersonDTO;
import rest_with_spring_boot_and_java.file.exporter.MediaTypes;
import rest_with_spring_boot_and_java.service.PersonService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/person")
@Tag(name = "People", description = "Endpoints to manage people")
public class PersonController implements PersonControllerDocs {

    @Autowired
    private PersonService personService;

    @GetMapping(value = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PersonDTO findById(@PathVariable("id") Long id) {
        return personService.findById(id);
    }

    @Override
    @GetMapping(value = "/export/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> export(@PathVariable("id") Long id, HttpServletRequest request) {

        String accpetHeader = request.getHeader(HttpHeaders.ACCEPT);

        Resource file = personService.exportPerson(id, accpetHeader);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(accpetHeader))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=person.pdf")
                .body(file);
    }


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(personService.findAll(pageable));
    }

    @GetMapping(value = "/findPeopleByName/{firstName}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findPeopleByName(@PathVariable("firstName") String firstName,
                                                                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                               @RequestParam(value = "size", defaultValue = "12") Integer size,
                                                                               @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(personService.findPeopleByName(firstName, pageable));

    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PersonDTO createPerson(@RequestBody PersonDTO person) {
        return personService.createPerson(person);
    }

    @PostMapping(value = "/create-many-people",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public List<PersonDTO> createManyPeople(@RequestParam("file") MultipartFile file) {
        return personService.createManyPeople(file);
    }

    @PutMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PersonDTO updatePerson(@RequestBody PersonDTO person) {
        return personService.updatePerson(person);
    }

    @DeleteMapping(value = "{id}")
    @Override
    public ResponseEntity<?> deletePerson(@PathVariable("id") Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PersonDTO disablePerson(Long id) {
        return personService.disablePerson(id);
    }

    @GetMapping(value = "export-page", produces = {MediaTypes.APPLICATION_XLSX_VALUE, MediaTypes.APPLICATION_CSV_VALUE, MediaTypes.APPLICATION_PDF_VALUE})
    @Override
    public ResponseEntity<Resource> exportPage(Integer page, Integer size, String direction, HttpServletRequest request) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));

        String accpetHeader = request.getHeader(HttpHeaders.ACCEPT);


        Resource file = personService.exportPage(pageable, accpetHeader);

        Map<String, String> extensionMap = Map.of(
                MediaTypes.APPLICATION_XLSX_VALUE, ".xlsx",
                MediaTypes.APPLICATION_CSV_VALUE, ".csv",
                MediaTypes.APPLICATION_PDF_VALUE, ".pdf"
        );

        String fileExtention = extensionMap.getOrDefault(accpetHeader, "");

        String contentType = accpetHeader != null ? accpetHeader : "application/octet-stream";


        String filename = "people_exported" + fileExtention;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(file);
    }


}
