
package com.aws.email_scheduler.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class ReportService {

    public String generateDailyReport() {

        LocalDate date = LocalDate.now(
                ZoneId.of("Asia/Kolkata")
        );

        int totalOrders = 120;
        int successfulOrders = 115;
        int failedOrders = 5;
        double totalRevenue = 25000.00;

        return """
                DAILY SALES REPORT
                ==============================
                
                Date: %s
                
                Total Orders      : %d
                Successful Orders : %d
                Failed Orders     : %d
                Total Revenue     : Rs. %.2f
                
                ==============================
                
                This is an automated daily report.
                
                Regards,
                Email Scheduler System
                """.formatted(
                date,
                totalOrders,
                successfulOrders,
                failedOrders,
                totalRevenue
        );
    }
}
