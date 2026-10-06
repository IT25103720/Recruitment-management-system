package com.lankajobshub.repository;

import com.lankajobshub.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByApplicationId(Long applicationId);

    @Query("SELECT i FROM Interview i WHERE i.application.applicant.id = :applicantId ORDER BY i.scheduledDate DESC")
    List<Interview> findByApplicantId(@Param("applicantId") Long applicantId);

    @Query("SELECT i FROM Interview i WHERE i.application.job.postedBy.id = :postedById ORDER BY i.scheduledDate DESC")
    List<Interview> findByJobPostedById(@Param("postedById") Long postedById);

    @Modifying
    @Query("DELETE FROM Interview i WHERE i.application.job.id = :jobId")
    void deleteByJobId(@Param("jobId") Long jobId);
}
