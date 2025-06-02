package rest_with_spring_boot_and_java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest_with_spring_boot_and_java.data.dto.PersonDTO;
import rest_with_spring_boot_and_java.handler.ResourceNotFoundException;
import static rest_with_spring_boot_and_java.mapper.ObjectMapper.parseObjectsLists;
import static rest_with_spring_boot_and_java.mapper.ObjectMapper.parseObject;
import rest_with_spring_boot_and_java.model.Person;
import rest_with_spring_boot_and_java.repository.PersonRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {

    private final AtomicLong counter = new AtomicLong();
    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository repository;


    public PersonDTO findById(Long id) {
        logger.info("Finding a person...");

        var entity = repository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        return parseObject(entity, PersonDTO.class);

    }


    public List<PersonDTO> findAll() {
        logger.info("Finding everyone...");
        return parseObjectsLists(repository.findAll(), PersonDTO.class);
    }

    public PersonDTO createPerson(PersonDTO person) {
        logger.info("Creating a person...");
        var entity = parseObject(person, Person.class);
        return parseObject(repository.save(entity), PersonDTO.class);
    }

    public PersonDTO updatePerson(PersonDTO person) {
        logger.info("Updating a person...");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        return parseObject(repository.save(entity), PersonDTO.class);
    }

    public void deletePerson(Long id) {
        logger.info("deleting a person...");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));
        repository.delete(entity);
    }

}
