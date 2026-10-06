package com.lankajobshub.service;

import com.lankajobshub.model.ApplicationStatus;
import com.lankajobshub.model.Interview;
import com.lankajobshub.model.Interview.InterviewStatus;
import com.lankajobshub.model.JobApplication;
import com.lankajobshub.model.Notification.NotificationType;
import com.lankajobshub.model.User;
import com.lankajobshub.model.UserRole;
import com.lankajobshub.repository.InterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterviewService {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    public Interview createInterview(Long applicationId, Interview interview, Long currentUserId) {
        User user = requireUser(currentUserId);
        JobApplication application = requireApplication(applicationId);
        requireManager(application, user);
        validateSchedulableApplication(application);
        validateInterview(interview, true);

        interview.setApplication(application);
        interview.setInterviewer(user);
        interview.setStatus(InterviewStatus.SCHEDULED);
        interview.setCreatedDate(LocalDateTime.now());
        interview.setUpdatedDate(LocalDateTime.now());

        Interview saved = interviewRepository.save(interview);
        jobApplicationService.updateApplicationStatus(application.getId(), ApplicationStatus.INTERVIEW_SCHEDULED, currentUserId, null);
        notifyApplicant(saved, "Interview scheduled", "An interview for " + saved.getApplication().getJob().getTitle() + " has been scheduled for " + saved.getScheduledDate() + ".");
        return saved;
    }

    public Interview updateInterview(Long id, Interview updated, Long currentUserId) {
        User user = requireUser(currentUserId);
        Interview existing = requireInterview(id);
        requireManager(existing.getApplication(), user);
        if (existing.getStatus() == InterviewStatus.CANCELLED || existing.getStatus() == InterviewStatus.COMPLETED) {
            throw new RuntimeException("Completed or cancelled interviews cannot be rescheduled");
        }
        validateInterview(updated, true);

        existing.setScheduledDate(updated.getScheduledDate());
        existing.setDurationMinutes(updated.getDurationMinutes());
        existing.setLocation(clean(updated.getLocation()));
        existing.setMeetingLink(clean(updated.getMeetingLink()));
        existing.setPanelInfo(clean(updated.getPanelInfo()));
        existing.setInterviewType(updated.getInterviewType());
        existing.setNotes(clean(updated.getNotes()));
        existing.setStatus(InterviewStatus.RESCHEDULED);
        existing.setUpdatedDate(LocalDateTime.now());
        Interview saved = interviewRepository.save(existing);
        notifyApplicant(saved, "Interview rescheduled", "Your interview for " + saved.getApplication().getJob().getTitle() + " has been rescheduled for " + saved.getScheduledDate() + ".");
        return saved;
    }

    public Interview completeInterview(Long id, String feedback, Long currentUserId) {
        User user = requireUser(currentUserId);
        Interview interview = requireInterview(id);
        requireManager(interview.getApplication(), user);
        if (interview.getStatus() == InterviewStatus.CANCELLED) {
            throw new RuntimeException("Cancelled interviews cannot be completed");
        }
        if (interview.getScheduledDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Future interviews cannot be marked completed");
        }
        interview.setFeedback(clean(feedback));
        interview.setStatus(InterviewStatus.COMPLETED);
        interview.setUpdatedDate(LocalDateTime.now());
        Interview saved = interviewRepository.save(interview);
        jobApplicationService.updateApplicationStatus(saved.getApplication().getId(), ApplicationStatus.INTERVIEWED, currentUserId, null);
        notifyApplicant(saved, "Interview result available", "Your interview for " + saved.getApplication().getJob().getTitle() + " has been completed." + (isBlank(saved.getFeedback()) ? "" : " Result: " + saved.getFeedback()));
        return saved;
    }

    public Interview cancelInterview(Long id, Long currentUserId) {
        User user = requireUser(currentUserId);
        Interview interview = requireInterview(id);
        requireManager(interview.getApplication(), user);
        if (interview.getStatus() == InterviewStatus.COMPLETED) {
            throw new RuntimeException("Completed interviews cannot be cancelled");
        }
        interview.setStatus(InterviewStatus.CANCELLED);
        interview.setUpdatedDate(LocalDateTime.now());
        Interview saved = interviewRepository.save(interview);
        if (saved.getApplication().getStatus() == ApplicationStatus.INTERVIEW_SCHEDULED) {
            jobApplicationService.updateApplicationStatus(saved.getApplication().getId(), ApplicationStatus.SHORTLISTED, currentUserId, null);
        }
        notifyApplicant(saved, "Interview cancelled", "Your interview for " + saved.getApplication().getJob().getTitle() + " has been cancelled.");
        return saved;
    }

    public void deleteInterview(Long id, Long currentUserId) {
        User user = requireUser(currentUserId);
        Interview interview = requireInterview(id);
        requireManager(interview.getApplication(), user);
        if (interview.getStatus() != InterviewStatus.CANCELLED) {
            throw new RuntimeException("Only cancelled interviews can be deleted");
        }
        interviewRepository.delete(interview);
    }

    public Interview findAuthorizedById(Long id, Long currentUserId) {
        User user = requireUser(currentUserId);
        Interview interview = requireInterview(id);
        requireReadable(interview, user);
        return interview;
    }

    public Interview findManageableById(Long id, Long currentUserId) {
        User user = requireUser(currentUserId);
        Interview interview = requireInterview(id);
        requireManager(interview.getApplication(), user);
        return interview;
    }

    public JobApplication findSchedulableApplication(Long applicationId, Long currentUserId) {
        User user = requireUser(currentUserId);
        JobApplication application = requireApplication(applicationId);
        requireManager(application, user);
        validateSchedulableApplication(application);
        return application;
    }

    public List<Interview> findRelevantForUser(Long currentUserId) {
        User user = requireUser(currentUserId);
        if (user.getRole() == UserRole.JOB_SEEKER) {
            return interviewRepository.findByApplicantId(user.getId());
        }
        if (user.getRole() == UserRole.ADMIN) {
            return interviewRepository.findAll();
        }
        return interviewRepository.findByJobPostedById(user.getId());
    }

    private Interview requireInterview(Long id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found with id: " + id));
    }

    private JobApplication requireApplication(Long applicationId) {
        return jobApplicationService.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));
    }

    private User requireUser(Long userId) {
        return userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    private void requireReadable(Interview interview, User user) {
        if (user.getRole() == UserRole.JOB_SEEKER) {
            if (!interview.getApplication().getApplicant().getId().equals(user.getId())) {
                throw new RuntimeException("You are not authorized to view this interview");
            }
            return;
        }
        requireManager(interview.getApplication(), user);
    }

    private void requireManager(JobApplication application, User user) {
        if (user.getRole() == UserRole.JOB_SEEKER) {
            throw new RuntimeException("Job seekers cannot manage interviews");
        }
        boolean owner = application.getJob().getPostedBy().getId().equals(user.getId());
        if (!owner && user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("You are not authorized to manage this interview");
        }
    }

    private void validateSchedulableApplication(JobApplication application) {
        if (application.getStatus() == ApplicationStatus.WITHDRAWN || application.getStatus() == ApplicationStatus.REJECTED) {
            throw new RuntimeException("Cannot schedule interviews for withdrawn or rejected applications");
        }
    }

    private void validateInterview(Interview interview, boolean requireFutureDate) {
        if (interview.getScheduledDate() == null) {
            throw new RuntimeException("Interview date and time are required");
        }
        if (requireFutureDate && !interview.getScheduledDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Interview date and time must be in the future");
        }
        if (interview.getInterviewType() == null) {
            throw new RuntimeException("Interview type is required");
        }
        if (isBlank(interview.getLocation()) && isBlank(interview.getMeetingLink())) {
            throw new RuntimeException("Location or meeting link is required");
        }
        if (interview.getDurationMinutes() == null || interview.getDurationMinutes() <= 0) {
            throw new RuntimeException("Duration must be greater than zero");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String clean(String value) {
        return value == null ? null : value.trim();
    }

    private void notifyApplicant(Interview interview, String title, String message) {
        notificationService.createNotification(
                interview.getApplication().getApplicant(),
                title,
                message,
                NotificationType.INTERVIEW
        );
    }
}
