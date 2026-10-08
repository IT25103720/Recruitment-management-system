package com.lankajobshub.model;

public enum ApplicationStatus {
    PENDING("Pending"),
    REVIEWING("Under Review"),
    SHORTLISTED("Shortlisted"),
    INTERVIEW_SCHEDULED("Interview Scheduled"),
    INTERVIEWED("Interviewed"),
    OFFERED("Job Offered"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected"),
    WITHDRAWN("Withdrawn");
    
    private final String displayName;
    
    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
