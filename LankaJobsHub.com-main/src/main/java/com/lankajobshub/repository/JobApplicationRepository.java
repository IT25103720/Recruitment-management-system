package com.lankajobshub.repository;

import com.lankajobshub.model.JobApplication;
import com.lankajobshub.model.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    
    List<JobApplication> findByJobId(Long jobId);
    
    List<JobApplication> findByApplicantId(Long applicantId);

    boolean existsByJobIdAndApplicantId(Long jobId, Long applicantId);
    
    List<JobApplication> findByStatus(ApplicationStatus status);
    
    @Query("SELECT ja FROM JobApplication ja WHERE ja.job.id = :jobId AND ja.status = :status")
    List<JobApplication> findByJobIdAndStatus(@Param("jobId") Long jobId, @Param("status") ApplicationStatus status);
    
    @Query("SELECT ja FROM JobApplication ja WHERE ja.applicant.id = :applicantId AND ja.status = :status")
    List<JobApplication> findByApplicantIdAndStatus(@Param("applicantId") Long applicantId, @Param("status") ApplicationStatus status);
    
    @Query("SELECT COUNT(ja) FROM JobApplication ja WHERE ja.job.id = :jobId")
    long countByJobId(@Param("jobId") Long jobId);
    
    @Query("SELECT COUNT(ja) FROM JobApplication ja WHERE ja.applicant.id = :applicantId")
    long countByApplicantId(@Param("applicantId") Long applicantId);
    
    @Query("SELECT ja FROM JobApplication ja WHERE ja.appliedDate >= :since")
    List<JobApplication> findApplicationsSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(ja) FROM JobApplication ja WHERE ja.status = :status")
    long countByStatus(@Param("status") ApplicationStatus status);
    
    @Query("SELECT ja FROM JobApplication ja WHERE ja.appliedDate > :date")
    List<JobApplication> findByAppliedDateAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT j.title, COUNT(ja) as applicationCount FROM Job j LEFT JOIN JobApplication ja ON j.id = ja.job.id GROUP BY j.id, j.title ORDER BY applicationCount DESC")
    List<Object[]> findTopJobsByApplicationCount();

    @Modifying
    @Query("DELETE FROM JobApplication ja WHERE ja.job.id = :jobId")
    void deleteByJobId(@Param("jobId") Long jobId);
}
