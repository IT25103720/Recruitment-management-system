package com.lankajobshub.model;

public enum UserRole {
    ADMIN("IT Officer"),
    CLIENT_RELATIONS_EXECUTIVE("Client Relations Executive"),
    COMPANY_HIRING_MANAGER("Company Hiring Manager"),
    RECRUITMENT_MANAGER("Recruitment Manager"),
    HR_ASSISTANT("HR Assistant"),
    JOB_SEEKER("Job Seeker");
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Check if a role can create accounts for another role
     */
    public boolean canCreateRole(UserRole targetRole) {
        switch (this) {
            case ADMIN:
                return targetRole == CLIENT_RELATIONS_EXECUTIVE;
            case CLIENT_RELATIONS_EXECUTIVE:
                return targetRole == COMPANY_HIRING_MANAGER;
            case COMPANY_HIRING_MANAGER:
                return targetRole == RECRUITMENT_MANAGER;
            case RECRUITMENT_MANAGER:
                return targetRole == HR_ASSISTANT;
            default:
                return false;
        }
    }
    
    /**
     * Get the roles that this role can create
     */
    public UserRole[] getCreatableRoles() {
        switch (this) {
            case ADMIN:
                return new UserRole[]{CLIENT_RELATIONS_EXECUTIVE};
            case CLIENT_RELATIONS_EXECUTIVE:
                return new UserRole[]{COMPANY_HIRING_MANAGER};
            case COMPANY_HIRING_MANAGER:
                return new UserRole[]{RECRUITMENT_MANAGER};
            case RECRUITMENT_MANAGER:
                return new UserRole[]{HR_ASSISTANT};
            default:
                return new UserRole[]{};
        }
    }
    
    /**
     * Check if this role can self-register
     */
    public boolean canSelfRegister() {
        return this == JOB_SEEKER;
    }
}
