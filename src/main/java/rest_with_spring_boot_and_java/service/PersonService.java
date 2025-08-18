package rest_with_spring_boot_and_java.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rest_with_spring_boot_and_java.controllers.PersonController;
import rest_with_spring_boot_and_java.data.dto.PersonDTO;
import rest_with_spring_boot_and_java.handler.RequiredObjectIsNullException;
import rest_with_spring_boot_and_java.handler.ResourceNotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static rest_with_spring_boot_and_java.mapper.ObjectMapper.parseObject;
import rest_with_spring_boot_and_java.model.Person;
import rest_with_spring_boot_and_java.repository.PersonRepository;

import java.util.logging.Logger;

@Service
public class PersonService {
    
    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository repository;


    public PersonDTO findById(Long id) {
        logger.info("Finding a person...");

        var entity = repository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        var dto = parseObject(entity, PersonDTO.class);

        addHateoasLinks(dto);

        return dto;

    }


    public Page<PersonDTO> findAll(Pageable pageable) {
        logger.info("Finding everyone...");

        var people = repository.findAll(pageable);

        return people.map(person -> {
            var dto = parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });
    }

    public PersonDTO createPerson(PersonDTO person) {
        if (person == null) {
            throw new RequiredObjectIsNullException();
        }
        logger.info("Creating a person...");
        var entity = parseObject(person, Person.class);
        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO updatePerson(PersonDTO person) {
        if (person == null) {
            throw new RequiredObjectIsNullException();
        }
        logger.info("Updating a person...");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void deletePerson(Long id) {
        logger.info("deleting a person...");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));
        repository.delete(entity);

        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);

    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("disabling a person...");
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        repository.disablePerson(id);

        var entity = repository.findById(id).get();

        return parseObject(entity, PersonDTO.class);

    }

    private static void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).deletePerson(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).createPerson(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).updatePerson(dto)).withRel("update").withType("PUT"));
    }

}
