package rest_with_spring_boot_and_java.file.exporter.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import rest_with_spring_boot_and_java.data.dto.PersonDTO;
import rest_with_spring_boot_and_java.file.exporter.contract.FileExporter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvExporter implements FileExporter {

    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        CSVFormat format = CSVFormat.Builder.create()
                .setHeader("ID", "First name", "Last name", "Address", "Gender", "Enabled")
                .setSkipHeaderRecord(false)
                .get();

        try (CSVPrinter printer = new CSVPrinter(writer, format)) {

            for (PersonDTO person : people) {
                printer.printRecord(
                        person.getId(),
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getGender(),
                        person.getEnabled()
                );
            }

        }

        return new ByteArrayResource(outputStream.toByteArray());
    }
}
