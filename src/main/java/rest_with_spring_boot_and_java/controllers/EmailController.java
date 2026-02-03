package rest_with_spring_boot_and_java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rest_with_spring_boot_and_java.controllers.docs.EmailControllerDocs;
import rest_with_spring_boot_and_java.data.dto.request.EmailRequestDTO;
import rest_with_spring_boot_and_java.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController implements EmailControllerDocs {

    @Autowired
    EmailService service;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO request) {
        service.sendSimpleEmail(request);
        return new ResponseEntity<>("E-mail sent with success! ", HttpStatus.OK);
    }

    @PostMapping("/with-attachment")
    @Override
    public ResponseEntity<String> sendEmailWithAttachment(@RequestParam("request") String request,
                                                          @RequestParam("attachment") MultipartFile attachment) {
        service.sendEmailWithAttachment(request, attachment);
        return new ResponseEntity<>("E-mail with attachment sent successfully! ", HttpStatus.OK);    }
}
