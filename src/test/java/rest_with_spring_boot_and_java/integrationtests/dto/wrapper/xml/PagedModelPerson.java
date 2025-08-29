package rest_with_spring_boot_and_java.integrationtests.dto.wrapper.xml;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rest_with_spring_boot_and_java.integrationtests.dto.PersonDTO;

import java.io.Serializable;
import java.util.List;

@XmlRootElement
@NoArgsConstructor
@Getter
@Setter
public class PagedModelPerson implements Serializable {

    @XmlElement(name = "content")
    public List<PersonDTO> content;

}
