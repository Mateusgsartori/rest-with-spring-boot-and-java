package rest_with_spring_boot_and_java.unittests.controllers.withxml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import rest_with_spring_boot_and_java.config.TestConfigs;
import rest_with_spring_boot_and_java.unittests.dto.BookDTO;
import rest_with_spring_boot_and_java.unittests.dto.wrapper.xml.PagedModelBook;
import rest_with_spring_boot_and_java.unittests.testcontainers.AbstractIntegrationTest;

import java.math.BigDecimal;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerXmlTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;
    private static BookDTO book;

    @BeforeAll
    static void init() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        book = new BookDTO();
    }

    @BeforeEach
    void setUp() {
        specification = new RequestSpecBuilder()
                .addHeader(
                        TestConfigs.HEADER_PARAM_ORIGIN,
                        "http://localhost:" + port
                )
                .setBasePath("/api/books")
                .setPort(port)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockBook();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        book = objectMapper.readValue(content, BookDTO.class);

        assertNotNull(book.getId());
        assertEquals("Docker Deep Dive", book.getTitle());
        assertEquals("Nigel Poulton", book.getAuthor());
        assertEquals(new BigDecimal("55.99"), book.getPrice());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        book.setTitle("Docker Deep Dive - Updated");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(book)
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        book = objectMapper.readValue(content, BookDTO.class);

        assertEquals("Docker Deep Dive - Updated", book.getTitle());
        assertEquals("Nigel Poulton", book.getAuthor());
        assertEquals(new BigDecimal("55.99"), book.getPrice());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        book = objectMapper.readValue(content, BookDTO.class);

        assertNotNull(book.getId());
        assertEquals("Working effectively with legacy code", book.getTitle());
        assertEquals("Michael C. Feathers", book.getAuthor());
        assertEquals(new BigDecimal("49.00"), book.getPrice());
    }

    @Test
    @Order(4)
    void deleteTest() {
        given(specification)
                .pathParam("id", book.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    void findAllTest() throws JsonProcessingException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        PagedModelBook wrapper =
                objectMapper.readValue(content, PagedModelBook.class);

        var books = wrapper.getContent();

        BookDTO bookOne = books.getFirst();

        assertNotNull(bookOne.getId());
        assertEquals("Design Patterns", bookOne.getTitle());
        assertEquals("Ralph Johnson, Erich Gamma, John Vlissides e Richard Helm", bookOne.getAuthor());
        assertEquals(new BigDecimal("45.00"), bookOne.getPrice());

        BookDTO bookFive = books.get(4);

        assertNotNull(bookFive.getId());
        assertEquals("Refactoring", bookFive.getTitle());
        assertEquals("Martin Fowler e Kent Beck", bookFive.getAuthor());
        assertEquals(new BigDecimal("88.00"), bookFive.getPrice());
    }

    private void mockBook() {
        book.setId(1L);
        book.setTitle("Docker Deep Dive");
        book.setAuthor("Nigel Poulton");
        book.setPrice(BigDecimal.valueOf(55.99));
        book.setLaunchDate(new Date());
    }
}