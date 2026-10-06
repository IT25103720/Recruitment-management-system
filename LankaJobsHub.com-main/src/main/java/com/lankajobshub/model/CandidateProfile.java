package com.lankajobshub.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidates")
public class CandidateProfile {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "resume_path")
    private String resumePath;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(columnDefinition = "TEXT")
    private String education;

    @Column(name = "experience", columnDefinition = "TEXT")
    private String workExperience;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_job_type")
    private JobType preferredJobType;

    @Column(name = "preferred_location")
    private String preferredLocation;

    @Column(name = "expected_salary", precision = 12, scale = 2)
    private BigDecimal expectedSalary;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getResumePath() { return resumePath; }
    public void setResumePath(String resumePath) { this.resumePath = resumePath; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getWorkExperience() { return workExperience; }
    public void setWorkExperience(String workExperience) { this.workExperience = workExperience; }

    public JobType getPreferredJobType() { return preferredJobType; }
    public void setPreferredJobType(JobType preferredJobType) { this.preferredJobType = preferredJobType; }

    public String getPreferredLocation() { return preferredLocation; }
    public void setPreferredLocation(String preferredLocation) { this.preferredLocation = preferredLocation; }

    public BigDecimal getExpectedSalary() { return expectedSalary; }
    public void setExpectedSalary(BigDecimal expectedSalary) { this.expectedSalary = expectedSalary; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
}
