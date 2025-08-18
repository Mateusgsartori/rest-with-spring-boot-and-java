package rest_with_spring_boot_and_java.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rest_with_spring_boot_and_java.controllers.docs.PersonControllerDocs;
import rest_with_spring_boot_and_java.data.dto.PersonDTO;
import rest_with_spring_boot_and_java.service.PersonService;

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


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<Page<PersonDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(personService.findAll(pageable));
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PersonDTO createPerson(@RequestBody PersonDTO person) {
        return personService.createPerson(person);
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


}
