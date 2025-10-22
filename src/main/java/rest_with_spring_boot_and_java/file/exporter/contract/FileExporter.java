package rest_with_spring_boot_and_java.file.exporter.contract;

import org.springframework.core.io.Resource;
import rest_with_spring_boot_and_java.data.dto.PersonDTO;

import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws Exception;

}
