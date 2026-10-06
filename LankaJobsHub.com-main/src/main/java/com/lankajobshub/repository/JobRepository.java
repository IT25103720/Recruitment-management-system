package com.lankajobshub.repository;

import com.lankajobshub.model.Job;
import com.lankajobshub.model.JobStatus;
import com.lankajobshub.model.JobType;
import com.lankajobshub.model.ExperienceLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    List<Job> findByStatus(JobStatus status);
    
    List<Job> findByJobType(JobType jobType);
    
    List<Job> findByExperienceLevel(ExperienceLevel level);
    
    List<Job> findByLocation(String location);
    
    List<Job> findByIndustry(String industry);
    
    List<Job> findByCompanyId(Long companyId);
    
    List<Job> findByPostedById(Long userId);
    
    @Query("SELECT j FROM Job j WHERE j.isActive = true")
    Page<Job> findActiveJobs(Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE j.isActive = true AND j.status = :status")
    List<Job> findActiveJobsByStatus(@Param("status") JobStatus status);
    
    @Query("SELECT j FROM Job j WHERE j.title LIKE %:keyword% OR j.description LIKE %:keyword% OR j.requirements LIKE %:keyword%")
    List<Job> searchJobs(@Param("keyword") String keyword);
    
    @Query("SELECT j FROM Job j WHERE j.postedDate >= :since")
    List<Job> findJobsPostedSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(j) FROM Job j WHERE j.status = :status")
    long countByStatus(@Param("status") JobStatus status);
    
    @Query("SELECT j FROM Job j WHERE j.deadline IS NOT NULL AND j.deadline <= :deadline")
    List<Job> findJobsByDeadline(@Param("deadline") LocalDateTime deadline);
    
    @Query("SELECT COUNT(j) FROM Job j WHERE j.experienceLevel = :level")
    long countByExperienceLevel(@Param("level") ExperienceLevel level);
    
    @Query("SELECT COUNT(j) FROM Job j WHERE j.jobType = :type")
    long countByJobType(@Param("type") JobType type);
    
    @Query("SELECT j FROM Job j WHERE j.postedDate > :date")
    List<Job> findByPostedDateAfter(@Param("date") LocalDateTime date);
}
