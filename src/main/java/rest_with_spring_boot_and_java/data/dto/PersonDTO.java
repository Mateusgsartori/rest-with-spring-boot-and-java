package rest_with_spring_boot_and_java.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import rest_with_spring_boot_and_java.model.Books;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({"id", "first_name", "last_name", "address", "gender"})
@Relation(collectionRelation = "people")
public class PersonDTO extends RepresentationModel<PersonDTO> implements Serializable {
    private Long id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String address;
    private String gender;
    private Boolean enabled;
    private String wikipediaProfileUrl;
    private String photoUrl;

    @JsonIgnore
    private List<Books> books;

    @JsonIgnore
    public String getName() {
        return (firstName != null ? firstName : "") +
                (lastName != null ?  " " + lastName : "");
    }
}
