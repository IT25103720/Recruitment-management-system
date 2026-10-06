package com.lankajobshub.controller;

import com.lankajobshub.model.Interview;
import com.lankajobshub.model.User;
import com.lankajobshub.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/interviews")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @GetMapping
    public String listInterviews(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login";
        }
        List<Interview> interviews = interviewService.findRelevantForUser(user.getId());
        model.addAttribute("interviews", interviews);
        model.addAttribute("user", user);
        return "interview/list";
    }

    @GetMapping("/{id}")
    public String viewInterview(@PathVariable Long id,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }
            model.addAttribute("interview", interviewService.findAuthorizedById(id, user.getId()));
            model.addAttribute("user", user);
            return "interview/view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/interviews";
        }
    }

    @GetMapping("/schedule/{applicationId}")
    public String showScheduleForm(@PathVariable Long applicationId,
                                 HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }
            model.addAttribute("interview", new Interview());
            model.addAttribute("application", interviewService.findSchedulableApplication(applicationId, user.getId()));
            model.addAttribute("interviewTypes", Interview.InterviewType.values());
            model.addAttribute("formAction", "/lankajobshub/interviews/schedule/" + applicationId);
            return "interview/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/interviews";
        }
    }

    @PostMapping("/schedule/{applicationId}")
    public String scheduleInterview(@PathVariable Long applicationId,
                                  @ModelAttribute Interview interview,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }
            Interview saved = interviewService.createInterview(applicationId, interview, user.getId());
            redirectAttributes.addFlashAttribute("success", "Interview scheduled successfully");
            return "redirect:/interviews/" + saved.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to schedule interview: " + e.getMessage());
            return "redirect:/interviews/schedule/" + applicationId;
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }
            Interview interview = interviewService.findManageableById(id, user.getId());
            model.addAttribute("interview", interview);
            model.addAttribute("application", interview.getApplication());
            model.addAttribute("interviewTypes", Interview.InterviewType.values());
            model.addAttribute("formAction", "/lankajobshub/interviews/" + id + "/edit");
            return "interview/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/interviews";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateInterview(@PathVariable Long id,
                                @ModelAttribute Interview interview,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }
            Interview saved = interviewService.updateInterview(id, interview, user.getId());
            redirectAttributes.addFlashAttribute("success", "Interview updated successfully");
            return "redirect:/interviews/" + saved.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update interview: " + e.getMessage());
            return "redirect:/interviews/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/complete")
    public String completeInterview(@PathVariable Long id,
                                  @RequestParam(required = false) String feedback,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }
            interviewService.completeInterview(id, feedback, user.getId());
            redirectAttributes.addFlashAttribute("success", "Interview marked completed");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to complete interview: " + e.getMessage());
        }
        return "redirect:/interviews/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancelInterview(@PathVariable Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }
            interviewService.cancelInterview(id, user.getId());
            redirectAttributes.addFlashAttribute("success", "Interview cancelled");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to cancel interview: " + e.getMessage());
        }
        return "redirect:/interviews/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteInterview(@PathVariable Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }
            interviewService.deleteInterview(id, user.getId());
            redirectAttributes.addFlashAttribute("success", "Interview deleted");
            return "redirect:/interviews";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete interview: " + e.getMessage());
            return "redirect:/interviews/" + id;
        }
    }
}
