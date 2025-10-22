package rest_with_spring_boot_and_java.unittests.controllers.withyaml;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import rest_with_spring_boot_and_java.config.TestConfigs;
import rest_with_spring_boot_and_java.unittests.dto.BookDTO;
import rest_with_spring_boot_and_java.unittests.dto.wrapper.xml.PagedModelBook;
import rest_with_spring_boot_and_java.unittests.testcontainers.AbstractIntegrationTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Nested
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper objectMapper;

    private static BookDTO book;

    @BeforeAll
    static void setUp() {
        objectMapper = new YAMLMapper();
        book = new BookDTO();
    }

    @Test
    @Order(1)
    void createTest() throws Exception {
        mockBook();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SUCCESS_TEST)
                .setBasePath("/api/books")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        String responseString = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                ).spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(objectMapper.writeValueAsString(book)) // serializa DTO -> YAML
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .asString(); // captura resposta como String

        BookDTO createdBook = objectMapper.readValue(responseString, BookDTO.class);

        book = createdBook;

        assertNotNull(createdBook.getId());
        assertEquals("Docker Deep Dive", book.getTitle());
        assertEquals("Nigel Poulton", book.getAuthor());
        assertEquals(BigDecimal.valueOf(55.99), book.getPrice());
    }

    @Test
    @Order(2)
    void updateTest() throws Exception {
        book.setTitle("Docker Deep Dive - Updated");

        String responseString = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                ).spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(objectMapper.writeValueAsString(book))
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .asString();

        BookDTO updatedBook = objectMapper.readValue(responseString, BookDTO.class);
        book = updatedBook;

        assertNotNull(book.getId());
        assertEquals("Docker Deep Dive - Updated", book.getTitle());
        assertEquals("Nigel Poulton", book.getAuthor());
        assertEquals(BigDecimal.valueOf(55.99), book.getPrice());
    }

    @Test
    @Order(3)
    void findByIdTest() throws Exception {
        String responseString = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                ).spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .asString();

        BookDTO foundBook = objectMapper.readValue(responseString, BookDTO.class);

        assertNotNull(foundBook.getId());
        assertTrue(foundBook.getId() > 0);
        assertEquals("Docker Deep Dive - Updated", foundBook.getTitle());
        assertEquals("Nigel Poulton", foundBook.getAuthor());
        assertEquals(BigDecimal.valueOf(55.99), foundBook.getPrice());
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
    void findAllTest() throws Exception {
        String responseString = given(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .asString();

        PagedModelBook response = objectMapper.readValue(responseString, PagedModelBook.class);
        List<BookDTO> content = response.getContent();

        BookDTO foundBookOne = content.getFirst();

        assertNotNull(foundBookOne.getId());
        assertTrue(foundBookOne.getId() > 0);
        assertEquals("Working effectively with legacy code", foundBookOne.getTitle());
        assertEquals("Michael C. Feathers", foundBookOne.getAuthor());
        assertEquals(new BigDecimal("49.00"), foundBookOne.getPrice());

        BookDTO foundBookFive = content.get(4);

        assertNotNull(foundBookFive.getId());
        assertTrue(foundBookFive.getId() > 0);
        assertEquals("Code complete", foundBookFive.getTitle());
        assertEquals("Steve McConnell", foundBookFive.getAuthor());
        assertEquals(new BigDecimal("58.00"), foundBookFive.getPrice());
    }

    private void mockBook() {
        book.setTitle("Docker Deep Dive");
        book.setAuthor("Nigel Poulton");
        book.setPrice(BigDecimal.valueOf(55.99));
        book.setLaunchDate(new Date());
    }
}
