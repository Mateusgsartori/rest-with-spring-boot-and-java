package rest_with_spring_boot_and_java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import rest_with_spring_boot_and_java.controllers.BookController;
import rest_with_spring_boot_and_java.data.dto.BookDTO;

import rest_with_spring_boot_and_java.handler.RequiredObjectIsNullException;
import rest_with_spring_boot_and_java.handler.ResourceNotFoundException;
import rest_with_spring_boot_and_java.model.Books;
import rest_with_spring_boot_and_java.repository.BookRepository;


import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static rest_with_spring_boot_and_java.mapper.ObjectMapper.parseObject;

@Service
public class BookService {
    
    private final Logger logger = Logger.getLogger(BookService.class.getName());

    @Autowired
    BookRepository repository;

    @Autowired
    PagedResourcesAssembler<BookDTO> assembler;


    public BookDTO findById(Long id) {
        logger.info("Finding a book...");

        var entity = repository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        var dto = parseObject(entity, BookDTO.class);

        addHateoasLinks(dto);

        return dto;

    }


    public PagedModel<EntityModel<BookDTO>> findAll(Pageable pageable) {
        logger.info("Finding all books...");
        var books =  repository.findAll(pageable);

        var booksWithLinks = books.map(b -> {
            var dto = parseObject(b, BookDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))).withSelfRel();

        return assembler.toModel(booksWithLinks, findAllLink);

    }

    public BookDTO createBook(BookDTO book) {
        if (book == null) {
            throw new RequiredObjectIsNullException();
        }
        logger.info("Creating a book...");
        var entity = parseObject(book, Books.class);
        var dto = parseObject(repository.save(entity), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO updateBook(BookDTO book) {
        if (book == null) {
            throw new RequiredObjectIsNullException();
        }
        logger.info("Updating a book...");
        Books entity = repository.findById(book.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());
        var dto = parseObject(repository.save(entity), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void deleteBook(Long id) {
        logger.info("deleting a book...");
        Books entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));
        repository.delete(entity);

        var dto = parseObject(entity, BookDTO.class);
        addHateoasLinks(dto);

    }

    private static void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).deleteBook(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(BookController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).createBook(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).updateBook(dto)).withRel("update").withType("PUT"));
    }

}
