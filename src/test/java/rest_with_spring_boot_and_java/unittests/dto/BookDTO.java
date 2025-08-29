package rest_with_spring_boot_and_java.unittests.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDTO implements Serializable {
    private Long id;
    private String author;
    @JsonProperty("launch_date")
    private Date launchDate;
    private BigDecimal price;
    private String title;
}
