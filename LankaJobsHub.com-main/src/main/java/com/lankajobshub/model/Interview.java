package com.lankajobshub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interviews")
public class Interview {

    public enum InterviewType {
        IN_PERSON("In Person"),
        PHONE("Phone"),
        VIDEO("Video");

        private final String displayName;

        InterviewType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum InterviewStatus {
        SCHEDULED("Scheduled"),
        RESCHEDULED("Rescheduled"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled");

        private final String displayName;

        InterviewStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private JobApplication application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interviewer_id", nullable = false)
    private User interviewer;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes = 60;

    private String location;

    @Column(name = "meeting_link")
    private String meetingLink;

    @Column(name = "panel_info", columnDefinition = "TEXT")
    private String panelInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_type", nullable = false)
    private InterviewType interviewType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status = InterviewStatus.SCHEDULED;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public JobApplication getApplication() { return application; }
    public void setApplication(JobApplication application) { this.application = application; }

    public User getInterviewer() { return interviewer; }
    public void setInterviewer(User interviewer) { this.interviewer = interviewer; }

    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }

    public String getPanelInfo() { return panelInfo; }
    public void setPanelInfo(String panelInfo) { this.panelInfo = panelInfo; }

    public InterviewType getInterviewType() { return interviewType; }
    public void setInterviewType(InterviewType interviewType) { this.interviewType = interviewType; }

    public InterviewStatus getStatus() { return status; }
    public void setStatus(InterviewStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
}
