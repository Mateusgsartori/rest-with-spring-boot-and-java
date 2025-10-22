package rest_with_spring_boot_and_java.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rest_with_spring_boot_and_java.controllers.PersonController;
import rest_with_spring_boot_and_java.data.dto.PersonDTO;
import rest_with_spring_boot_and_java.exception.BadRequestException;
import rest_with_spring_boot_and_java.exception.FileStorageException;
import rest_with_spring_boot_and_java.exception.RequiredObjectIsNullException;
import rest_with_spring_boot_and_java.exception.ResourceNotFoundException;
import rest_with_spring_boot_and_java.file.exporter.contract.FileExporter;
import rest_with_spring_boot_and_java.file.exporter.factory.FileExporterFactory;
import rest_with_spring_boot_and_java.file.importer.contract.FileImporter;
import rest_with_spring_boot_and_java.file.importer.factory.FileImporterFactory;
import rest_with_spring_boot_and_java.model.Person;
import rest_with_spring_boot_and_java.repository.PersonRepository;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static rest_with_spring_boot_and_java.mapper.ObjectMapper.parseObject;

@Service
public class PersonService {

    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    FileImporterFactory importer;

    @Autowired
    FileExporterFactory exporter;

    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;

    public PersonDTO findById(Long id) {
        logger.info("Finding a person...");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;

    }

    public Resource exportPage(Pageable pageable, String acceptHeader) {
        logger.info("Exporting a people page");
        var people = repository.findAll(pageable)
                .map(p -> parseObject(p, PersonDTO.class)).getContent();

        try {
            FileExporter exporter = this.exporter.getExporter(acceptHeader);
            return exporter.exportFile(people);
        } catch (Exception e) {
            throw new RuntimeException("Error while exporting file!", e);
        }
    }

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("Finding everyone...");
        var people = repository.findAll(pageable);
        return buildPagedModel(pageable, people);
    }

    public PagedModel<EntityModel<PersonDTO>> findPeopleByName(String firstName, Pageable pageable) {
        var people = repository.findPeopleByName(firstName, pageable);
        return buildPagedModel(pageable, people);
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

    public List<PersonDTO> createManyPeople(MultipartFile file) {
        logger.info("Importing people from file");

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Please, send a valid file!");
        }

        try (InputStream inputStream = file.getInputStream()) {
            String fileName = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new BadRequestException("File name cannot be null"));

            FileImporter importer = this.importer.getImporter(fileName);

            List<Person> entities = importer.importFile(inputStream).stream()
                    .map(p -> repository.save(parseObject(p, Person.class)))
                    .toList();
            return entities.stream().map(entity -> {
                var dto = parseObject(entity, PersonDTO.class);
                addHateoasLinks(dto);
                return dto;
            }).toList();
        } catch (Exception e) {
            throw new FileStorageException("Error processing file!");
        }


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
        var entity = repository.findById(id).isPresent() ? repository.findById(id).get() : new Object();
        return parseObject(entity, PersonDTO.class);
    }

    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<Person> people) {
        var peopleWithLinks = people.map(person -> {
            var dto = parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });
        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))).withSelfRel();
        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    private static void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).deletePerson(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findPeopleByName("", 1, 12, "asc")).withRel("findPeopleByName").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).createPerson(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class)).slash("create-many-people").withRel("create-many-people").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).updatePerson(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).exportPage(
                 1, 12, "asc", null))
                .withRel("export-page")
                .withType("GET")
                .withTitle("Export people"));
    }

}
