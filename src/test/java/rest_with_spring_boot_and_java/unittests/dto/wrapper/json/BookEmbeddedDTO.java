package rest_with_spring_boot_and_java.unittests.dto.wrapper.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import rest_with_spring_boot_and_java.unittests.dto.BookDTO;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class BookEmbeddedDTO implements Serializable {

    @JsonProperty("bookDTOList")
    private List<BookDTO> books;

    public BookEmbeddedDTO() {}

}
