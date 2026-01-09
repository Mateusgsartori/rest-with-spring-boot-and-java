package rest_with_spring_boot_and_java.file.exporter.impl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import rest_with_spring_boot_and_java.data.dto.PersonDTO;
import rest_with_spring_boot_and_java.file.exporter.contract.PersonExporter;
import rest_with_spring_boot_and_java.service.QRCodeService;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfExporter implements PersonExporter {

    @Autowired
    QRCodeService service;

    @Override
    public Resource exportPeople(List<PersonDTO> people) throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/reports/people.jrxml");

        if (inputStream == null) {
            throw new RuntimeException("Template file '/reports/people.jrxml' not found!");
        }

        JasperReport jasper = JasperCompileManager.compileReport(inputStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);

        Map<String, Object> parameters = new HashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters, dataSource);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }

    }

    @Override
    public Resource exportPerson(PersonDTO person) throws Exception {
        InputStream mainTemplateStream = getClass().getResourceAsStream("/reports/person.jrxml");

        if (mainTemplateStream == null) {
            throw new RuntimeException("Template file '/reports/person.jrxml' not found!");
        }

        InputStream subReportStream = getClass().getResourceAsStream("/reports/books.jrxml");

        if (subReportStream == null) {
            throw new RuntimeException("Template file '/reports/person.jrxml' not found!");
        }

        JasperReport mainReport = JasperCompileManager.compileReport(mainTemplateStream);
        JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

        JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(Collections.singletonList(person));
        JRBeanCollectionDataSource subDataSource = new JRBeanCollectionDataSource(person.getBooks());

        InputStream qrCodeStream = service.generateQrCode(person.getWikipediaProfileUrl(), 200, 200);

        String path = String.valueOf(getClass().getResource("/reports/books.jasper"));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("SUB_REPORT_DATA_SOURCE", subDataSource);
        parameters.put("BOOK_SUB_REPORT", subReport);
        parameters.put("SUB_REPORT_DIR", path);
        parameters.put("QR_CODE_IAMGE", qrCodeStream);

        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, mainDataSource);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }


}
