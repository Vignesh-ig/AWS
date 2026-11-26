package com.aws.email_scheduler.service;

import com.aws.email_scheduler.config.SesProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.util.List;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);


    private final SesClient sesClient;
    private final SesProperties sesProperties;

    public EmailService(SesClient sesClient, SesProperties sesProperties) {
        this.sesClient = sesClient;
        this.sesProperties = sesProperties;
    }

    public void sendEmail(String subject, String body) {

        List<String> recipients = sesProperties.getRecipients();

        long startTime = System.nanoTime();
        int successCount = 0;
        int failureCount = 0;
        logger.info("Total recipients: {}", recipients.size());

        for (String recipient : recipients) {
            try {
                sendIndividualEmail(recipient, subject, body);
                successCount++;

            } catch (Exception e) {
                failureCount++;
                logger.error("Failed to send email to: {}", recipient, e);

            }
        }

        long endTime = System.nanoTime();

        double executionTime = (endTime - startTime) / 1_000_000.0;

        logger.info("Total recipients: {}", recipients.size());
        logger.info("Successful requests: {}", successCount);
        logger.info("Failed requests: {}", failureCount);
        logger.info("Execution time: {} ms", executionTime);

        if (failureCount > 0) {
            throw new IllegalStateException("Email batch completed with failures");
        }
    }

    public void sendIndividualEmail(String recipient, String subject, String body) {
        Destination destination = Destination.builder()
                .toAddresses(recipient)
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

        logger.info("Email accepted for: {} | Message ID: {}", recipient, sendEmailResponse.messageId());

    }
}
