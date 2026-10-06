package com.lankajobshub.service;

import com.lankajobshub.model.CandidateProfile;
import com.lankajobshub.model.User;
import com.lankajobshub.model.UserRole;
import com.lankajobshub.repository.CandidateProfileRepository;
import com.lankajobshub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CandidateProfileService {

    @Autowired
    private CandidateProfileRepository candidateProfileRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<CandidateProfile> findByUserId(Long userId) {
        return candidateProfileRepository.findById(userId);
    }

    public CandidateProfile saveForUser(Long userId, CandidateProfile candidateProfile) {
        User user = requireJobSeeker(userId);
        validate(candidateProfile);

        CandidateProfile existing = candidateProfileRepository.findById(userId).orElseGet(CandidateProfile::new);
        if (existing.getCreatedDate() == null) {
            existing.setCreatedDate(LocalDateTime.now());
        }
        existing.setUser(user);
        existing.setResumePath(user.getResumePath());
        existing.setSkills(clean(candidateProfile.getSkills()));
        existing.setEducation(clean(candidateProfile.getEducation()));
        existing.setWorkExperience(clean(candidateProfile.getWorkExperience()));
        existing.setPreferredJobType(candidateProfile.getPreferredJobType());
        existing.setPreferredLocation(clean(candidateProfile.getPreferredLocation()));
        existing.setExpectedSalary(candidateProfile.getExpectedSalary());
        existing.setUpdatedDate(LocalDateTime.now());

        return candidateProfileRepository.save(existing);
    }

    public void updateResumePath(Long userId, String resumePath) {
        User user = requireJobSeeker(userId);
        CandidateProfile profile = candidateProfileRepository.findById(userId).orElseGet(CandidateProfile::new);
        if (profile.getCreatedDate() == null) {
            profile.setCreatedDate(LocalDateTime.now());
        }
        profile.setUser(user);
        profile.setResumePath(resumePath);
        profile.setUpdatedDate(LocalDateTime.now());
        candidateProfileRepository.save(profile);
    }

    public void deleteForUser(Long userId) {
        requireJobSeeker(userId);
        if (candidateProfileRepository.existsById(userId)) {
            candidateProfileRepository.deleteById(userId);
        }
    }

    private User requireJobSeeker(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        if (user.getRole() != UserRole.JOB_SEEKER) {
            throw new RuntimeException("Only job seekers can manage candidate profile data");
        }
        return user;
    }

    private void validate(CandidateProfile candidateProfile) {
        BigDecimal expectedSalary = candidateProfile.getExpectedSalary();
        if (expectedSalary != null && expectedSalary.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Expected salary cannot be negative");
        }
    }

    private String clean(String value) {
        return value == null ? null : value.trim();
    }
}
