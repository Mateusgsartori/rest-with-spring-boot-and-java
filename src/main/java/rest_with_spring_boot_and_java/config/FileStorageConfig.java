package rest_with_spring_boot_and_java.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file")
@NoArgsConstructor
@Getter
@Setter
public class FileStorageConfig {

    private String uploadDir;



}
