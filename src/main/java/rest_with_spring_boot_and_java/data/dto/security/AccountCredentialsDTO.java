package rest_with_spring_boot_and_java.data.dto.security;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AccountCredentialsDTO implements Serializable {

    private String username;
    private String password;
    private String fullName;

}
