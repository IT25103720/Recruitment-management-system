package com.lankajobshub.service;

import com.lankajobshub.model.Job;
import com.lankajobshub.model.JobApplication;
import com.lankajobshub.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    // For now, this is a simulated email service
    // In a real implementation, you would use JavaMailSender or similar

    public void sendJobApplicationNotification(JobApplication application) {
        String subject = "Job Application Received - " + application.getJob().getTitle();
        String message = buildApplicationNotificationMessage(application);
        
        // Simulate sending email
        System.out.println("=== EMAIL NOTIFICATION ===");
        System.out.println("To: " + application.getJob().getPostedBy().getEmail());
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println("========================");
    }

    public void sendApplicationStatusUpdate(JobApplication application) {
        String subject = "Application Status Update - " + application.getJob().getTitle();
        String message = buildStatusUpdateMessage(application);
        
        // Simulate sending email
        System.out.println("=== EMAIL NOTIFICATION ===");
        System.out.println("To: " + application.getApplicant().getEmail());
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println("========================");
    }

    public void sendWelcomeEmail(User user) {
        String subject = "Welcome to LankaJobsHub!";
        String message = buildWelcomeMessage(user);
        
        // Simulate sending email
        System.out.println("=== EMAIL NOTIFICATION ===");
        System.out.println("To: " + user.getEmail());
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println("========================");
    }

    public void sendJobPostedNotification(Job job) {
        String subject = "Job Posted Successfully - " + job.getTitle();
        String message = buildJobPostedMessage(job);
        
        // Simulate sending email
        System.out.println("=== EMAIL NOTIFICATION ===");
        System.out.println("To: " + job.getPostedBy().getEmail());
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println("========================");
    }

    private String buildApplicationNotificationMessage(JobApplication application) {
        StringBuilder message = new StringBuilder();
        message.append("Dear ").append(application.getJob().getPostedBy().getEmail()).append(",\n\n");
        message.append("A new job application has been received for the position: ").append(application.getJob().getTitle()).append("\n\n");
        message.append("Applicant: ").append(application.getApplicant().getEmail()).append("\n");
        message.append("Applied Date: ").append(application.getAppliedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
        message.append("Status: ").append(application.getStatus()).append("\n\n");
        message.append("Please log in to your dashboard to review this application.\n\n");
        message.append("Best regards,\nLankaJobsHub Team");
        
        return message.toString();
    }

    private String buildStatusUpdateMessage(JobApplication application) {
        StringBuilder message = new StringBuilder();
        message.append("Dear ").append(application.getApplicant().getEmail()).append(",\n\n");
        message.append("Your application status has been updated for the position: ").append(application.getJob().getTitle()).append("\n\n");
        message.append("New Status: ").append(application.getStatus()).append("\n");
        message.append("Updated Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n\n");
        
        if (application.getStatus().name().equals("REJECTED") && application.getRejectionReason() != null) {
            message.append("Reason: ").append(application.getRejectionReason()).append("\n\n");
        }
        
        message.append("Please log in to your account for more details.\n\n");
        message.append("Best regards,\nLankaJobsHub Team");
        
        return message.toString();
    }

    private String buildWelcomeMessage(User user) {
        StringBuilder message = new StringBuilder();
        message.append("Dear ").append(user.getEmail()).append(",\n\n");
        message.append("Welcome to LankaJobsHub! Your account has been successfully created.\n\n");
        message.append("Account Details:\n");
        message.append("- Email: ").append(user.getEmail()).append("\n");
        message.append("- Role: ").append(user.getRole()).append("\n");
        message.append("- Status: ").append(user.getStatus()).append("\n\n");
        message.append("You can now:\n");
        message.append("- Browse available jobs\n");
        message.append("- Apply for positions\n");
        message.append("- Manage your profile\n");
        message.append("- Track your applications\n\n");
        message.append("If you have any questions, please don't hesitate to contact our support team.\n\n");
        message.append("Best regards,\nLankaJobsHub Team");
        
        return message.toString();
    }

    private String buildJobPostedMessage(Job job) {
        StringBuilder message = new StringBuilder();
        message.append("Dear ").append(job.getPostedBy().getEmail()).append(",\n\n");
        message.append("Your job posting has been successfully created!\n\n");
        message.append("Job Details:\n");
        message.append("- Title: ").append(job.getTitle()).append("\n");
        message.append("- Company: ").append(job.getCompany() != null ? job.getCompany().getName() : "N/A").append("\n");
        message.append("- Location: ").append(job.getLocation()).append("\n");
        message.append("- Posted Date: ").append(job.getPostedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
        message.append("- Status: ").append(job.getStatus()).append("\n\n");
        message.append("Your job is now visible to potential candidates. You will receive notifications when applications are submitted.\n\n");
        message.append("Best regards,\nLankaJobsHub Team");
        
        return message.toString();
    }
}
