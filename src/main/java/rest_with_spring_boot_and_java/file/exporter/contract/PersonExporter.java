package rest_with_spring_boot_and_java.file.exporter.contract;

import org.springframework.core.io.Resource;
import rest_with_spring_boot_and_java.data.dto.PersonDTO;

import java.util.List;

public interface PersonExporter {

    Resource exportPeople(List<PersonDTO> people) throws Exception;
    Resource exportPerson(PersonDTO person) throws Exception;

}
