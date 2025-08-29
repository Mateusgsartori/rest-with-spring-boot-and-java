package rest_with_spring_boot_and_java.integrationtests.dto.wrapper.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WrapperPersonDTO implements Serializable {

    @JsonProperty(value = "_embedded")
    private PersonEmbeddedDTO embedded;


}
