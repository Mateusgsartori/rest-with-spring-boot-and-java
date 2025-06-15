package rest_with_spring_boot_and_java.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("REST API")
                        .version("v1")
                        .description("REST API")
                        .termsOfService("google.com")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("google.com")));
    }




}
