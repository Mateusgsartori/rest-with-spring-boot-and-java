package rest_with_spring_boot_and_java.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EmailConfig {

    private String host;  //smtp.gmail.com
    private int port; //: 587
    private String username; //: ${EMAIL_USERNAME}
    private String password; //: ${EMAIL_PASSWORD}
    private String from;
    private boolean ssl;

}
