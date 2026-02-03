package rest_with_spring_boot_and_java.data.dto.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class EmailRequestDTO {
    private String to;
    private String subject;
    private String boddy;

}
