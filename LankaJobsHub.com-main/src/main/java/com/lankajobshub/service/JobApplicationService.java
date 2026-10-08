package com.lankajobshub.service;

import com.lankajobshub.model.JobApplication;
import com.lankajobshub.model.ApplicationStatus;
import com.lankajobshub.model.Job;
import com.lankajobshub.model.Notification.NotificationType;
import com.lankajobshub.model.User;
import com.lankajobshub.model.UserRole;
import com.lankajobshub.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class JobApplicationService {

    @Autowired
    private JobService jobService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    public JobApplication createApplication(Long jobId, Long applicantId, String coverLetter, String additionalNotes) {
        Job job = jobService.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        User applicant = userService.findById(applicantId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + applicantId));

        if (applicant.getRole() != UserRole.JOB_SEEKER) {
            throw new RuntimeException("Only job seekers can submit applications");
        }

        if (jobApplicationRepository.existsByJobIdAndApplicantId(jobId, applicantId)) {
            throw new RuntimeException("You have already applied for this job");
        }

        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setApplicant(applicant);
        application.setCoverLetter(coverLetter);
        application.setAdditionalNotes(additionalNotes);
        application.setStatus(ApplicationStatus.PENDING);
        application.setAppliedDate(LocalDateTime.now());
        application.setActive(true);

        JobApplication saved = jobApplicationRepository.save(application);

        notificationService.createNotification(
                saved.getJob().getPostedBy(),
                "New application received",
                saved.getApplicant().getEmail() + " applied for " + saved.getJob().getTitle() + ".",
                NotificationType.APPLICATION
        );

        try {
            emailService.sendJobApplicationNotification(saved);
        } catch (Exception e) {
            System.err.println("Error sending application notification: " + e.getMessage());
        }

        return saved;
    }

    public Optional<JobApplication> findById(Long id) {
        return jobApplicationRepository.findById(id);
    }

    public List<JobApplication> findByJobId(Long jobId) {
        return jobApplicationRepository.findByJobId(jobId);
    }

    public List<JobApplication> findByApplicantId(Long applicantId) {
        return jobApplicationRepository.findByApplicantId(applicantId);
    }

    public List<JobApplication> findByStatus(ApplicationStatus status) {
        return jobApplicationRepository.findByStatus(status);
    }

    public List<JobApplication> findByJobIdAndStatus(Long jobId, ApplicationStatus status) {
        return jobApplicationRepository.findByJobIdAndStatus(jobId, status);
    }

    public List<JobApplication> findByApplicantIdAndStatus(Long applicantId, ApplicationStatus status) {
        return jobApplicationRepository.findByApplicantIdAndStatus(applicantId, status);
    }

    public long countApplicationsByJobId(Long jobId) {
        return jobApplicationRepository.countByJobId(jobId);
    }

    public long countApplicationsByApplicantId(Long applicantId) {
        return jobApplicationRepository.countByApplicantId(applicantId);
    }

    public JobApplication updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        return updateApplicationStatus(applicationId, status, null, null);
    }

    public JobApplication updateApplicationStatus(Long applicationId, ApplicationStatus status, Long reviewerId, String rejectionReason) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));
        application.setStatus(status);
        application.setReviewedDate(LocalDateTime.now());

        if (reviewerId != null) {
            User reviewer = userService.findById(reviewerId)
                    .orElseThrow(() -> new RuntimeException("Reviewer not found with id: " + reviewerId));
            application.setReviewedBy(reviewer);
        }

        if (status == ApplicationStatus.REJECTED) {
            application.setRejectionReason(rejectionReason);
        } else {
            application.setRejectionReason(null);
        }

        JobApplication saved = jobApplicationRepository.save(application);

        String message = "Your application for " + saved.getJob().getTitle() + " is now " + saved.getStatus().getDisplayName() + ".";
        if (saved.getStatus() == ApplicationStatus.REJECTED && saved.getRejectionReason() != null && !saved.getRejectionReason().trim().isEmpty()) {
            message += " Reason: " + saved.getRejectionReason();
        }
        notificationService.createNotification(
                saved.getApplicant(),
                saved.getStatus() == ApplicationStatus.REJECTED ? "Application rejected" : "Application status updated",
                message,
                NotificationType.APPLICATION
        );

        try {
            emailService.sendApplicationStatusUpdate(saved);
        } catch (Exception e) {
            System.err.println("Error sending status update notification: " + e.getMessage());
        }

        return saved;
    }

    public JobApplication updateApplicationDetails(Long applicationId, Long applicantId, String coverLetter, String additionalNotes) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

        validateApplicantCanManage(application, applicantId);
        application.setCoverLetter(coverLetter);
        application.setAdditionalNotes(additionalNotes);
        return jobApplicationRepository.save(application);
    }

    public boolean deleteApplication(Long id) {
        if (jobApplicationRepository.existsById(id)) {
            jobApplicationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteApplication(Long applicationId, Long applicantId) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));
        validateApplicantCanManage(application, applicantId);
        jobApplicationRepository.deleteById(applicationId);
    }

    public List<JobApplication> findRecentApplications() {
        // Example: last 7 days via repository custom query could be added; fallback to fetching all and filtering
        LocalDateTime since = LocalDateTime.now().minusDays(7);
        return jobApplicationRepository.findApplicationsSince(since);
    }

    public long countAllApplications() {
        return jobApplicationRepository.count();
    }

    public List<JobApplication> findAll() {
        return jobApplicationRepository.findAll();
    }

    public boolean canApplicantModify(JobApplication application, Long applicantId) {
        if (application == null || application.getApplicant() == null || !application.getApplicant().getId().equals(applicantId)) {
            return false;
        }
        if (application.getStatus() != ApplicationStatus.PENDING && application.getStatus() != ApplicationStatus.REVIEWING) {
            return false;
        }
        return application.getJob().getDeadline() == null || application.getJob().getDeadline().isAfter(LocalDateTime.now());
    }

    private void validateApplicantCanManage(JobApplication application, Long applicantId) {
        if (!canApplicantModify(application, applicantId)) {
            throw new RuntimeException("This application can no longer be modified");
        }
    }
}
