package com.lankajobshub.service;

import com.lankajobshub.model.Job;
import com.lankajobshub.model.JobStatus;
import com.lankajobshub.model.JobType;
import com.lankajobshub.model.ExperienceLevel;
import com.lankajobshub.model.User;
import com.lankajobshub.model.UserRole;
import com.lankajobshub.model.UserStatus;
import com.lankajobshub.repository.InterviewRepository;
import com.lankajobshub.repository.JobApplicationRepository;
import com.lankajobshub.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    public Job createJob(Job job, Long postedByUserId) {
        User user = userService.findById(postedByUserId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + postedByUserId));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new RuntimeException("User account is not active");
        }

        if (!canManageVacancies(user.getRole())) {
            throw new RuntimeException("You are not authorized to post jobs");
        }

        validateJob(job, true);
        job.setPostedBy(user);
        job.setStatus(JobStatus.PENDING); // Default to pending for admin approval
        job.setPostedDate(LocalDateTime.now());
        syncActiveFlag(job);

        Job saved = jobRepository.save(job);

        try {
            emailService.sendJobPostedNotification(saved);
        } catch (Exception e) {
            System.err.println("Error sending job posted notification: " + e.getMessage());
        }

        return saved;
    }

    public Optional<Job> findById(Long id) {
        return jobRepository.findById(id);
    }

    public List<Job> findAllActiveJobs() {
        return jobRepository.findByStatus(JobStatus.ACTIVE).stream()
                .filter(Job::isActive)
                .collect(Collectors.toList());
    }

    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    public List<Job> findByCompany(Long companyId) {
        return jobRepository.findByCompanyId(companyId);
    }

    public List<Job> findByPostedBy(Long userId) {
        return jobRepository.findByPostedById(userId);
    }

    public List<Job> findByJobType(JobType jobType) {
        return jobRepository.findByJobType(jobType);
    }

    public List<Job> findByStatus(JobStatus status) {
        return jobRepository.findByStatus(status);
    }

    public List<Job> findByExperienceLevel(ExperienceLevel level) {
        return jobRepository.findByExperienceLevel(level);
    }

    public List<Job> findByLocation(String location) {
        return jobRepository.findByLocation(location);
    }

    public List<Job> findByIndustry(String industry) {
        return jobRepository.findByIndustry(industry);
    }

    public List<Job> searchJobs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return jobRepository.findAll();
        }
        return jobRepository.searchJobs(keyword);
    }

    public List<Job> findJobs(String keyword, String location, JobType jobType, ExperienceLevel experienceLevel) {
        List<Job> base = (keyword == null || keyword.trim().isEmpty())
                ? jobRepository.findAll()
                : jobRepository.searchJobs(keyword);

        return base.stream()
                .filter(job -> location == null || location.trim().isEmpty() ||
                        (job.getLocation() != null && job.getLocation().toLowerCase().contains(location.trim().toLowerCase())))
                .filter(job -> jobType == null || job.getJobType() == jobType)
                .filter(job -> experienceLevel == null || job.getExperienceLevel() == experienceLevel)
                .collect(Collectors.toList());
    }

    public Job updateJob(Job job) {
        return updateJob(job, null);
    }

    public Job updateJob(Job job, Long currentUserId) {
        Job existing = jobRepository.findById(job.getId())
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + job.getId()));

        if (currentUserId != null) {
            requireOwnerOrAdmin(existing, currentUserId);
        }

        validateJob(job, false);
        existing.setTitle(job.getTitle());
        existing.setDescription(job.getDescription());
        existing.setRequirements(job.getRequirements());
        existing.setResponsibilities(job.getResponsibilities());
        existing.setJobType(job.getJobType());
        existing.setExperienceLevel(job.getExperienceLevel());
        existing.setSalaryMin(job.getSalaryMin());
        existing.setSalaryMax(job.getSalaryMax());
        existing.setLocation(job.getLocation());
        existing.setIndustry(job.getIndustry());
        existing.setDeadline(job.getDeadline());
        syncActiveFlag(existing);

        return jobRepository.save(existing);
    }

    public boolean deleteJob(Long id) {
        return deleteJob(id, null);
    }

    @Transactional
    public boolean deleteJob(Long id, Long currentUserId) {
        if (currentUserId != null) {
            Job job = jobRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
            requireOwnerOrAdmin(job, currentUserId);
        }

        if (jobRepository.existsById(id)) {
            interviewRepository.deleteByJobId(id);
            jobApplicationRepository.deleteByJobId(id);
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Job changeJobStatus(Long jobId, JobStatus status) {
        return changeJobStatus(jobId, status, null);
    }

    public Job changeJobStatus(Long jobId, JobStatus status, Long currentUserId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
        if (currentUserId != null) {
            requireOwnerOrAdmin(job, currentUserId);
        }
        job.setStatus(status);
        syncActiveFlag(job);
        return jobRepository.save(job);
    }

    private void validateJob(Job job, boolean creating) {
        if (isBlank(job.getTitle())) {
            throw new RuntimeException("Job title is required");
        }
        if (isBlank(job.getDescription())) {
            throw new RuntimeException("Job description is required");
        }
        if (isBlank(job.getRequirements())) {
            throw new RuntimeException("Job requirements are required");
        }
        if (job.getJobType() == null) {
            throw new RuntimeException("Job type is required");
        }
        if (job.getExperienceLevel() == null) {
            throw new RuntimeException("Experience level is required");
        }
        if (isBlank(job.getLocation())) {
            throw new RuntimeException("Location is required");
        }
        if (job.getSalaryMin() != null && job.getSalaryMin().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Minimum salary cannot be negative");
        }
        if (job.getSalaryMax() != null && job.getSalaryMax().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Maximum salary cannot be negative");
        }
        if (job.getSalaryMin() != null && job.getSalaryMax() != null && job.getSalaryMin().compareTo(job.getSalaryMax()) > 0) {
            throw new RuntimeException("Minimum salary cannot exceed maximum salary");
        }
        if (job.getDeadline() != null && job.getDeadline().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Application deadline cannot be in the past");
        }
    }

    private void requireOwnerOrAdmin(Job job, Long currentUserId) {
        User user = userService.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + currentUserId));
        boolean owner = job.getPostedBy() != null && job.getPostedBy().getId().equals(currentUserId);
        if (!owner && user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("You are not authorized to manage this job");
        }
    }

    private boolean canManageVacancies(UserRole role) {
        return role == UserRole.ADMIN
                || role == UserRole.CLIENT_RELATIONS_EXECUTIVE
                || role == UserRole.COMPANY_HIRING_MANAGER
                || role == UserRole.RECRUITMENT_MANAGER
                || role == UserRole.HR_ASSISTANT;
    }

    private void syncActiveFlag(Job job) {
        job.setActive(job.getStatus() == JobStatus.ACTIVE);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
