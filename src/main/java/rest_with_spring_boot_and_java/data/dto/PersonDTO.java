package rest_with_spring_boot_and_java.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PersonDTO implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
}
