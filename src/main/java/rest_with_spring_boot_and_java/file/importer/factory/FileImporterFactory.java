package rest_with_spring_boot_and_java.file.importer.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import rest_with_spring_boot_and_java.exception.BadRequestException;
import rest_with_spring_boot_and_java.file.importer.contract.FileImporter;
import rest_with_spring_boot_and_java.file.importer.impl.CsvImporter;
import rest_with_spring_boot_and_java.file.importer.impl.XlsxImporter;

@Component
public class FileImporterFactory {

    private Logger log = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public FileImporter getImporter(String fileName) throws Exception {
        if (fileName.endsWith(".xlsx")) {
            return context.getBean(XlsxImporter.class);
        } else if (fileName.endsWith(".csv")) {
            return  context.getBean(CsvImporter.class);
        } else {
            throw new BadRequestException("Invalid file format!");
        }
    }

}
