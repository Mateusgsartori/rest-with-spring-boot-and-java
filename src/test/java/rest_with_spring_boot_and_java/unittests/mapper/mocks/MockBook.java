package rest_with_spring_boot_and_java.unittests.mapper.mocks;

import rest_with_spring_boot_and_java.data.dto.BookDTO;
import rest_with_spring_boot_and_java.model.Books;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MockBook {


    public Books mockEntity() {
        return mockEntity(0);
    }

    public BookDTO mockDTO() {
        return mockDTO(0);
    }

    public List<Books> mockEntityList() {
        List<Books> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDTO> mockDTOList() {
        List<BookDTO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockDTO(i));
        }
        return books;
    }

    public Books mockEntity(Integer number) {
        Books book = new Books();
        book.setId(number.longValue());
        book.setAuthor("Some Author" + number);
        book.setLaunchDate(new Date());
        book.setPrice(BigDecimal.valueOf(25D));
        book.setTitle("Some Title" + number);
        return book;
    }

    public BookDTO mockDTO(Integer number) {
        BookDTO book = new BookDTO();
        book.setId(number.longValue());
        book.setAuthor("Some Author" + number);
        book.setLaunchDate(new Date());
        book.setPrice(BigDecimal.valueOf(25D));
        book.setTitle("Some Title" + number);
        return book;
    }

}