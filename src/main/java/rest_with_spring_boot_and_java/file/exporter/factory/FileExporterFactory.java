package rest_with_spring_boot_and_java.file.exporter.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import rest_with_spring_boot_and_java.exception.BadRequestException;
import rest_with_spring_boot_and_java.file.exporter.MediaTypes;
import rest_with_spring_boot_and_java.file.exporter.contract.FileExporter;
import rest_with_spring_boot_and_java.file.exporter.impl.CsvExporter;
import rest_with_spring_boot_and_java.file.exporter.impl.XlsxExporter;
import rest_with_spring_boot_and_java.file.importer.factory.FileImporterFactory;


@Component
public class FileExporterFactory {

    private Logger log = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public FileExporter getExporter(String acceptHeader) throws Exception {

        if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
            return context.getBean(XlsxExporter.class);
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
            return  context.getBean(CsvExporter.class);
        } else {
            throw new BadRequestException("Invalid file format!");
        }
    }

}
