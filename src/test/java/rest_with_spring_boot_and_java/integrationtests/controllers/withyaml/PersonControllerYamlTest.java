package rest_with_spring_boot_and_java.integrationtests.controllers.withyaml;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import rest_with_spring_boot_and_java.config.TestConfigs;
import rest_with_spring_boot_and_java.integrationtests.dto.PersonDTO;
import rest_with_spring_boot_and_java.integrationtests.dto.wrapper.xml.PagedModelPerson;
import rest_with_spring_boot_and_java.integrationtests.testcontainers.AbstractIntegrationTest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerYamlTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    private static RequestSpecification specification;
    private static YAMLMapper objectMapper;
    private static PersonDTO person;

    @BeforeAll
    static void init() {
        objectMapper = new YAMLMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        person = new PersonDTO();
    }

    @BeforeEach
    void setUp() {
        specification = new RequestSpecBuilder()
                .addHeader(
                        TestConfigs.HEADER_PARAM_ORIGIN,
                        "http://localhost:" + port
                )
                .setBasePath("/api/person")
                .setPort(port)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    private RequestSpecification yamlRequest() {
        return given()
                .config(RestAssuredConfig.config()
                        .encoderConfig(encoderConfig()
                                .encodeContentTypeAs(
                                        "application/yaml",
                                        io.restassured.http.ContentType.TEXT
                                )))
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
        assertEquals("Benedict Torvalds", person.getLastName());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {
        var content = yamlRequest()
                .contentType("application/yaml")
                .accept("application/yaml")
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType("application/yaml")
                .extract()
                .body()
                .asString();

        person = objectMapper.readValue(content, PersonDTO.class);

        assertNotNull(person.getId());
        assertEquals("Benedict Torvalds", person.getLastName());
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
    void findAllTest() throws Exception {
        String yaml = given(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .queryParams("page", 3, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .asString();

        PagedModelPerson wrapper = objectMapper.readValue(yaml, PagedModelPerson.class);
        List<PersonDTO> list = wrapper.getContent();

        person = list.getFirst();

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertTrue(person.getId() > 0);

        assertEquals("Allin", person.getFirstName());
        assertEquals("Emmot", person.getLastName());
        assertEquals("7913 Lindbergh Way", person.getAddress());
        assertEquals("Male", person.getGender());
        assertFalse(person.getEnabled());

        person = list.get(1);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertTrue(person.getId() > 0);

        assertEquals("Allin", person.getFirstName());
        assertEquals("Otridge", person.getLastName());
        assertEquals("7913 Lindbergh Way", person.getAddress());
        assertEquals("Male", person.getGender());
        assertFalse(person.getEnabled());

        person = list.get(5);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertTrue(person.getId() > 0);

        assertEquals("Alphonso", person.getFirstName());
        assertEquals("Eddisforth", person.getLastName());
        assertEquals("485 Dayton Avenue", person.getAddress());
        assertEquals("Male", person.getGender());
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
