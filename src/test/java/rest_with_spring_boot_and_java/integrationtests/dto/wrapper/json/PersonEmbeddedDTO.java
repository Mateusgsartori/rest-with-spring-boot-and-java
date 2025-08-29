package rest_with_spring_boot_and_java.integrationtests.dto.wrapper.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rest_with_spring_boot_and_java.integrationtests.dto.PersonDTO;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PersonEmbeddedDTO implements Serializable {

    @JsonProperty(value = "people")
    private List<PersonDTO> people;

}
