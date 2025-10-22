package rest_with_spring_boot_and_java.file.importer.contract;

import rest_with_spring_boot_and_java.data.dto.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;

}
