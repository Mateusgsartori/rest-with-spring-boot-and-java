package rest_with_spring_boot_and_java.integrationtests.controllers.withyaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import rest_with_spring_boot_and_java.config.TestConfigs;
import rest_with_spring_boot_and_java.integrationtests.dto.PersonDTO;
import rest_with_spring_boot_and_java.integrationtests.testcontainers.AbstractIntegrationTest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper objectMapper;
    private static PersonDTO person;

    @BeforeAll
    static void setUp() {
        objectMapper = new YAMLMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        person = new PersonDTO();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SUCCESS_TEST)
                .setBasePath("/api/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    // Helper para configurar RestAssured para enviar YAML corretamente
    private RequestSpecification yamlRequest() {
        return given()
                .config(RestAssuredConfig.config()
                        .encoderConfig(encoderConfig()
                                .encodeContentTypeAs("application/yaml", io.restassured.http.ContentType.TEXT)))
                .spec(specification);
    }

    @Test
    @Order(1)
    void createPerson() throws JsonProcessingException {
        mockPerson();
        String yamlBody = objectMapper.writeValueAsString(person);

        var content = yamlRequest()
                .contentType("application/yaml")
                .accept("application/yaml")
                .body(yamlBody)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType("application/yaml")
                .extract()
                .body()
                .asString();

        person = objectMapper.readValue(content, PersonDTO.class);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertTrue(person.getId() > 0);

        assertEquals("Linus", person.getFirstName());
        assertEquals("Torvalds", person.getLastName());
        assertEquals("Helsinki", person.getAddress());
        assertEquals("Male", person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        person.setLastName("Benedict Torvalds");
        String yamlBody = objectMapper.writeValueAsString(person);

        var content = yamlRequest()
                .contentType("application/yaml")
                .accept("application/yaml")
                .body(yamlBody)
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType("application/yaml")
                .extract()
                .body()
                .asString();

        person = objectMapper.readValue(content, PersonDTO.class);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertTrue(person.getId() > 0);

        assertEquals("Linus", person.getFirstName());
        assertEquals("Benedict Torvalds", person.getLastName());
        assertEquals("Helsinki", person.getAddress());
        assertEquals("Male", person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {
        var content = yamlRequest()
                .contentType("application/yaml")
                .accept("application/yaml")
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .contentType("application/yaml")
                .extract()
                .body()
                .asString();

        person = objectMapper.readValue(content, PersonDTO.class);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertTrue(person.getId() > 0);

        assertEquals("Linus", person.getFirstName());
        assertEquals("Benedict Torvalds", person.getLastName());
        assertEquals("Helsinki", person.getAddress());
        assertEquals("Male", person.getGender());
        assertFalse(person.getEnabled());
    }

    @Test
    @Order(4)
    void disableTest() throws JsonProcessingException {
        var content = yamlRequest()
                .contentType("application/yaml")
                .accept("application/yaml")
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .contentType("application/yaml")
                .extract()
                .body()
                .asString();

        person = objectMapper.readValue(content, PersonDTO.class);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertTrue(person.getId() > 0);

        assertEquals("Linus", person.getFirstName());
        assertEquals("Benedict Torvalds", person.getLastName());
        assertEquals("Helsinki", person.getAddress());
        assertEquals("Male", person.getGender());
        assertFalse(person.getEnabled());
    }

    @Test
    @Order(5)
    void deleteTest() {
        yamlRequest()
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    void findAllTest() throws JsonProcessingException {
        var content = yamlRequest()
                .accept("application/yaml")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType("application/yaml")
                .extract()
                .body()
                .asString();

        List<PersonDTO> list = objectMapper.readValue(content, new TypeReference<List<PersonDTO>>() {});

        person = list.get(0);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertTrue(person.getId() > 0);

        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals("123 Main St, New York, NY", person.getAddress());
        assertEquals("male", person.getGender());
        assertTrue(person.getEnabled());

        person = list.get(1);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertTrue(person.getId() > 0);

        assertEquals("Jane", person.getFirstName());
        assertEquals("Smith", person.getLastName());
        assertEquals("456 Oak Ave, San Francisco, CA", person.getAddress());
        assertEquals("female", person.getGender());
        assertTrue(person.getEnabled());

        person = list.get(5);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertTrue(person.getId() > 0);

        assertEquals("Laura", person.getFirstName());
        assertEquals("Wilson", person.getLastName());
        assertEquals("987 Cedar Ln, Denver, CO", person.getAddress());
        assertEquals("female", person.getGender());
        assertTrue(person.getEnabled());
    }

    private void mockPerson() {
        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("Helsinki");
        person.setGender("Male");
        person.setEnabled(true);
    }
}