package rest_with_spring_boot_and_java.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rest_with_spring_boot_and_java.config.EmailConfig;
import rest_with_spring_boot_and_java.data.dto.request.EmailRequestDTO;
import rest_with_spring_boot_and_java.mail.EmailSender;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfigs;


    public void sendSimpleEmail(EmailRequestDTO request) {
        emailSender.to(request.getTo())
                .withSubject(request.getSubject())
                .withMessage(request.getBoddy()).send(emailConfigs);
    }

    public void sendEmailWithAttachment(String request, MultipartFile attachment) {
        File tempFile = null;
        try {
            EmailRequestDTO dto = new ObjectMapper().readValue(request, EmailRequestDTO.class);
            tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);

            emailSender.to(dto.getTo())
                    .withSubject(dto.getSubject())
                    .withMessage(dto.getBoddy())
                    .attach(tempFile.getAbsolutePath())
                    .send(emailConfigs);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing email request JSON!", e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing email attachment!", e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

}
