package rest_with_spring_boot_and_java.unittests.dto.wrapper.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import rest_with_spring_boot_and_java.unittests.dto.BookDTO;

import java.util.List;

@Setter
@Getter
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagedModelBook { 
	
	@XmlElement(name = "content") 
	private List<BookDTO> content;

	public PagedModelBook() {}

}
