package rest_with_spring_boot_and_java.controllers.docs;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rest_with_spring_boot_and_java.data.dto.security.AccountCredentialsDTO;

public interface AuthControllerDocs {
    @Operation(summary = "Authenticate an user and returns token")
    ResponseEntity<?> signin(AccountCredentialsDTO credentials);

    @Operation(summary = "Refresh token for authenticated users")
    ResponseEntity<?> refreshToken(String username, String refreshToken);

    AccountCredentialsDTO create(AccountCredentialsDTO user);
}
