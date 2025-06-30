package rest_with_spring_boot_and_java.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class BookDTO extends RepresentationModel<BookDTO> implements Serializable {

    private Long id;
    private String author;
    @JsonProperty("launch_date")
    private Date launchDate;
    private BigDecimal price;
    private String title;

}
