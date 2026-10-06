package com.lankajobshub.controller;

import com.lankajobshub.model.Job;
import com.lankajobshub.model.JobStatus;
import com.lankajobshub.model.JobType;
import com.lankajobshub.model.ExperienceLevel;
import com.lankajobshub.model.User;
import com.lankajobshub.model.UserRole;
import com.lankajobshub.service.JobService;
import com.lankajobshub.model.Company;
import com.lankajobshub.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping
    public String listJobs(Model model, 
                          @RequestParam(required = false) String keyword,
                          @RequestParam(required = false) String location,
                          @RequestParam(required = false) JobType jobType,
                          @RequestParam(required = false) ExperienceLevel experienceLevel) {
        
        List<Job> jobs = jobService.findJobs(keyword, location, jobType, experienceLevel);
        
        model.addAttribute("jobs", jobs);
        model.addAttribute("jobTypes", JobType.values());
        model.addAttribute("experienceLevels", ExperienceLevel.values());
        model.addAttribute("keyword", keyword);
        model.addAttribute("location", location);
        model.addAttribute("selectedJobType", jobType);
        model.addAttribute("selectedExperienceLevel", experienceLevel);
        
        return "job/list";
    }

    @GetMapping("/{id}")
    public String viewJob(@PathVariable Long id, Model model) {
        jobService.findById(id).ifPresent(job -> {
            model.addAttribute("job", job);
        });
        return "job/view";
    }

    @GetMapping("/post")
    public String showPostJobForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login";
        }

        if (!canManageVacancies(user.getRole())) {
            return "redirect:/jobs";
        }
        
        model.addAttribute("job", new Job());
        model.addAttribute("jobTypes", JobType.values());
        model.addAttribute("experienceLevels", ExperienceLevel.values());
        // Provide companies for management users to select from
        if (user.getRole() != UserRole.JOB_SEEKER) {
            model.addAttribute("companies", companyRepository.findAll());
        }
        return "job/post";
    }

    @PostMapping("/post")
    public String postJob(@ModelAttribute Job job, 
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }

            if (!canManageVacancies(user.getRole())) {
                throw new RuntimeException("You are not authorized to post jobs");
            }

            // Set or resolve company (schema requires company_id NOT NULL)
            if (job.getCompany() == null) {
                Company company = companyRepository.findByName("LankaJobsHub Tech").orElseGet(() -> {
                    Company c = new Company();
                    c.setName("LankaJobsHub Tech");
                    c.setContactPerson("Admin Team");
                    return companyRepository.save(c);
                });
                job.setCompany(company);
            } else if (job.getCompany().getId() != null) {
                // Resolve to a managed entity by id
                companyRepository.findById(job.getCompany().getId()).ifPresent(job::setCompany);
            }

            Job postedJob = jobService.createJob(job, user.getId());
            redirectAttributes.addFlashAttribute("success", "Job posted successfully!");
            return "redirect:/jobs/" + postedJob.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to post job: " + e.getMessage());
            return "redirect:/jobs/post";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditJobForm(@PathVariable Long id, 
                                 HttpSession session, 
                                 Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login";
        }

        Job job = jobService.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));

        if (!job.getPostedBy().getId().equals(user.getId()) && user.getRole() != UserRole.ADMIN) {
            return "redirect:/jobs/" + id;
        }

        model.addAttribute("job", job);
        model.addAttribute("jobTypes", JobType.values());
        model.addAttribute("experienceLevels", ExperienceLevel.values());
        
        return "job/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateJob(@PathVariable Long id, 
                           @ModelAttribute Job job,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }

            job.setId(id);
            Job updatedJob = jobService.updateJob(job, user.getId());
            redirectAttributes.addFlashAttribute("success", "Job updated successfully!");
            return "redirect:/jobs/" + updatedJob.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update job: " + e.getMessage());
            return "redirect:/jobs/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteJob(@PathVariable Long id, 
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }

            jobService.deleteJob(id, user.getId());
            redirectAttributes.addFlashAttribute("success", "Job deleted successfully");
            return "redirect:/jobs";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete job: " + e.getMessage());
            return "redirect:/jobs/" + id;
        }
    }

    @PostMapping("/{id}/status")
    public String changeJobStatus(@PathVariable Long id, 
                                 @RequestParam JobStatus status,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }

            jobService.changeJobStatus(id, status, user.getId());
            redirectAttributes.addFlashAttribute("success", "Job status updated successfully");
            return "redirect:/jobs/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update job status: " + e.getMessage());
            return "redirect:/jobs/" + id;
        }
    }

    @GetMapping("/my-jobs")
    public String showMyJobs(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login";
        }

        List<Job> myJobs = user.getRole() == UserRole.ADMIN
                ? jobService.findAll()
                : jobService.findByPostedBy(user.getId());
        model.addAttribute("jobs", myJobs);
        return "job/my-jobs";
    }

    private boolean canManageVacancies(UserRole role) {
        return role == UserRole.ADMIN
                || role == UserRole.CLIENT_RELATIONS_EXECUTIVE
                || role == UserRole.COMPANY_HIRING_MANAGER
                || role == UserRole.RECRUITMENT_MANAGER
                || role == UserRole.HR_ASSISTANT;
    }
}
