package com.aws.email_scheduler.scheduler;

import com.aws.email_scheduler.service.EmailService;
import com.aws.email_scheduler.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DailyReportScheduler {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    public final EmailService emailService;
    public final ReportService reportService;

    public DailyReportScheduler(EmailService emailService, ReportService reportService) {
        this.emailService = emailService;
        this.reportService = reportService;
    }

    @Scheduled(cron = "${scheduler.report.corn}", zone = "${scheduler.report.zone}")
    public void sendDailyReport() {

        logger.info("Daily report scheduler triggered");

        try {
            String subject = "Daily report on " + LocalDate.now();

            String report = reportService.generateDailyReport();
            emailService.sendEmail(subject, report);

            logger.info("Daily report accepted by AWS SES");

        } catch (Exception e) {
            logger.error("Failed to send daily report", e);

        }

    }
}
