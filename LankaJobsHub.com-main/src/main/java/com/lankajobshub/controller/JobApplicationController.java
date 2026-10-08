package com.lankajobshub.controller;

import com.lankajobshub.model.JobApplication;
import com.lankajobshub.model.ApplicationStatus;
import com.lankajobshub.model.User;
import com.lankajobshub.model.UserRole;
import com.lankajobshub.service.JobApplicationService;
import com.lankajobshub.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/applications")
public class JobApplicationController {

    @Autowired
    private JobApplicationService applicationService;

    @Autowired
    private JobService jobService;

    @GetMapping("/apply/{jobId}")
    public String showApplicationForm(@PathVariable Long jobId, 
                                    HttpSession session, 
                                    Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login?redirect=/applications/apply/" + jobId;
        }

        if (user.getRole() != UserRole.JOB_SEEKER) {
            return "redirect:/jobs/" + jobId;
        }

        jobService.findById(jobId).ifPresent(job -> {
            model.addAttribute("job", job);
        });
        
        model.addAttribute("application", new JobApplication());
        return "application/apply";
    }

    @PostMapping("/apply/{jobId}")
    public String submitApplication(@PathVariable Long jobId,
                                  @ModelAttribute JobApplication application,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }
            if (user.getRole() != UserRole.JOB_SEEKER) {
                redirectAttributes.addFlashAttribute("error", "Only job seekers can apply for jobs");
                return "redirect:/jobs/" + jobId;
            }

            applicationService.createApplication(
                jobId, user.getId(), application.getCoverLetter(), application.getAdditionalNotes()
            );
            
            redirectAttributes.addFlashAttribute("success", "Application submitted successfully!");
            return "redirect:/applications/my-applications";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to submit application: " + e.getMessage());
            return "redirect:/applications/apply/" + jobId;
        }
    }

    @GetMapping("/my-applications")
    public String showMyApplications(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login";
        }
        if (user.getRole() != UserRole.JOB_SEEKER) {
            return "redirect:/dashboard";
        }

        List<JobApplication> applications = applicationService.findByApplicantId(user.getId());
        model.addAttribute("applications", applications);
        return "application/my-applications";
    }

    @GetMapping("/{applicationId}/edit")
    public String showEditApplication(@PathVariable Long applicationId,
                                      HttpSession session,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login";
        }

        JobApplication application = applicationService.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));
        if (!applicationService.canApplicantModify(application, user.getId())) {
            redirectAttributes.addFlashAttribute("error", "This application can no longer be edited");
            return "redirect:/applications/my-applications";
        }

        model.addAttribute("application", application);
        return "application/edit";
    }

    @PostMapping("/{applicationId}/edit")
    public String updateApplication(@PathVariable Long applicationId,
                                    @RequestParam String coverLetter,
                                    @RequestParam(required = false) String additionalNotes,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }

            applicationService.updateApplicationDetails(applicationId, user.getId(), coverLetter, additionalNotes);
            redirectAttributes.addFlashAttribute("success", "Application updated successfully");
            return "redirect:/applications/my-applications";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update application: " + e.getMessage());
            return "redirect:/applications/" + applicationId + "/edit";
        }
    }

    @GetMapping("/job/{jobId}")
    public String showJobApplications(@PathVariable Long jobId,
                                    HttpSession session,
                                    Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login";
        }

        // Check if user is the job poster or admin
        jobService.findById(jobId).ifPresent(job -> {
            if (job.getPostedBy().getId().equals(user.getId()) || user.getRole().name().equals("ADMIN")) {
                List<JobApplication> applications = applicationService.findByJobId(jobId);
                model.addAttribute("job", job);
                model.addAttribute("applications", applications);
                model.addAttribute("statuses", ApplicationStatus.values());
            }
        });
        
        return "application/job-applications";
    }

    @PostMapping("/{applicationId}/status")
    public String updateApplicationStatus(@PathVariable Long applicationId,
                                        @RequestParam ApplicationStatus status,
                                        @RequestParam(required = false) String rejectionReason,
                                        HttpSession session,
                                        RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }

            JobApplication application = applicationService.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));
            Long jobId = application.getJob().getId();

            if (!application.getJob().getPostedBy().getId().equals(user.getId()) && user.getRole() != UserRole.ADMIN) {
                redirectAttributes.addFlashAttribute("error", "You are not authorized to update this application");
                return "redirect:/applications/job/" + jobId;
            }

            applicationService.updateApplicationStatus(applicationId, status, user.getId(), rejectionReason);
            redirectAttributes.addFlashAttribute("success", "Application status updated successfully");
            return "redirect:/applications/job/" + jobId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update application status: " + e.getMessage());
            return applicationService.findById(applicationId)
                    .map(application -> "redirect:/applications/job/" + application.getJob().getId())
                    .orElse("redirect:/jobs");
        }
    }

    @PostMapping("/{applicationId}/withdraw")
    public String withdrawApplication(@PathVariable Long applicationId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }

            JobApplication application = applicationService.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));
            if (!application.getApplicant().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "You are not authorized to withdraw this application");
                return "redirect:/applications/my-applications";
            }

            applicationService.updateApplicationStatus(applicationId, ApplicationStatus.WITHDRAWN);
            redirectAttributes.addFlashAttribute("success", "Application withdrawn successfully");
            return "redirect:/applications/my-applications";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to withdraw application: " + e.getMessage());
            return "redirect:/applications/my-applications";
        }
    }

    @PostMapping("/{applicationId}/delete")
    public String deleteApplication(@PathVariable Long applicationId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }

            applicationService.deleteApplication(applicationId, user.getId());
            redirectAttributes.addFlashAttribute("success", "Application deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete application: " + e.getMessage());
        }
        return "redirect:/applications/my-applications";
    }
}
