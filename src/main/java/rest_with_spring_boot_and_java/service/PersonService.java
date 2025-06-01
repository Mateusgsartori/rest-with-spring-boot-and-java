package rest_with_spring_boot_and_java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest_with_spring_boot_and_java.handler.ResourceNotFoundException;
import rest_with_spring_boot_and_java.model.Person;
import rest_with_spring_boot_and_java.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {

    private final AtomicLong counter = new AtomicLong();
    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository repository;


    public Person findById(Long id) {
        logger.info("Finding a person...");
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));
    }


    public List<Person> findAll() {
        logger.info("Finding everyone...");
        return repository.findAll();
    }

    public Person createPerson(Person person) {
        logger.info("Creating a person...");
        return repository.save(person);
    }

    public Person updatePerson(Person person) {
        logger.info("Updating a person...");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        repository.save(entity);

        return person;
    }

    public void deletePerson(Long id) {
        logger.info("deleting a person...");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));
        repository.delete(entity);
    }

    private Person mockPerson(int i) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Person " + i);
        person.setLastName("LastName " + i);
        person.setAddress("Address " + i);
        person.setGender("Gender " + i);
        return person;
    }

}
