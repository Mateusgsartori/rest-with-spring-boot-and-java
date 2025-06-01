package rest_with_spring_boot_and_java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rest_with_spring_boot_and_java.model.Person;
import rest_with_spring_boot_and_java.service.PersonService;

import javax.print.attribute.standard.Media;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person findById(@PathVariable("id") Long  id) {
        return personService.findById(id);
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> findAll() {
        return personService.findAll();
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public Person createPerson(@RequestBody Person person) {
        return personService.createPerson(person);
    }

    @PutMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public Person UpdatePerson(@RequestBody Person person) {
        return personService.updatePerson(person);
    }

    @DeleteMapping(value = "{id}" )
    public ResponseEntity<?> deletePerson(@PathVariable ("id") Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }


}
