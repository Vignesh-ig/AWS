package com.aws.email_scheduler.service;

import com.aws.email_scheduler.config.SesProperties;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.util.List;

@Service
public class EmailService {

    private final SesClient sesClient;
    private final SesProperties sesProperties;

    public EmailService(SesClient sesClient, SesProperties sesProperties) {
        this.sesClient = sesClient;
        this.sesProperties = sesProperties;
    }

    public void sendEmail(String subject, String body) {

        List<String> recipients = sesProperties.getRecipients();

        Destination destination = Destination.builder()
                .toAddresses(recipients)
                .ccAddresses("vigneshkwork7@gmail.com")
                .build();

        Content emailSubject = Content.builder().data(subject).charset("UTF-8").build();

        Content emailContent = Content.builder().data(body).charset("UTF-8").build();

        Body emailBody = Body.builder().text(emailContent).build();

        Message emailMessage = Message.builder().subject(emailSubject).body(emailBody).build();

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .source(sesProperties.getSender())
                .message(emailMessage)
                .destination(destination)
                .build();

        SendEmailResponse sendEmailResponse = sesClient.sendEmail(sendEmailRequest);

        System.out.println("Email accepted by Amazon SES!");
        System.out.println("Total recipients: " + recipients.size());
        System.out.println("Message ID: " + sendEmailResponse.messageId());

    }
}
