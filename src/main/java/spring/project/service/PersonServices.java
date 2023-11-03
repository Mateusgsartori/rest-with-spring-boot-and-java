package spring.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.project.exceptions.ResourceNotFoundEception;
import spring.project.model.Person;
import spring.project.repositories.PersonRepository;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    public List<Person> findAll() {
        logger.info("Finding all people");
        return repository.findAll();
    }

    public Person create(Person person) {
        logger.info("Creating person");

        repository.save(person);

        return person;
    }

    public Person update(Person person) {
        logger.info("Updating person");
        Person entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundEception("No record found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        repository.save(entity);

        return person;
    }


    public Person findById(Long id) {
        logger.info("Finding one person");
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundEception("No records found for this ID!"));
    }

    public void delete(Long id) {
        logger.info("Deleting person");
        Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundEception("No record found for this ID!"));
        repository.delete(entity);

    }

}
