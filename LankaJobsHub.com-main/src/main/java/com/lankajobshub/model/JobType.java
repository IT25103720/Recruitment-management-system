package com.lankajobshub.model;

public enum JobType {
    FULL_TIME("Full Time"),
    PART_TIME("Part Time"),
    CONTRACT("Contract"),
    INTERNSHIP("Internship"),
    FREELANCE("Freelance"),
    TEMPORARY("Temporary");
    
    private final String displayName;
    
    JobType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
