package com.lankajobshub.model;

public enum ExperienceLevel {
    ENTRY_LEVEL("Entry Level"),
    JUNIOR("Junior"),
    MID_LEVEL("Mid Level"),
    SENIOR("Senior"),
    LEAD("Lead"),
    MANAGER("Manager"),
    DIRECTOR("Director"),
    EXECUTIVE("Executive");
    
    private final String displayName;
    
    ExperienceLevel(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
