package com.lankajobshub.controller;

import com.lankajobshub.model.Notification;
import com.lankajobshub.model.NotificationTemplate;
import com.lankajobshub.model.User;
import com.lankajobshub.model.UserRole;
import com.lankajobshub.service.NotificationService;
import com.lankajobshub.service.NotificationTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationTemplateService notificationTemplateService;

    @GetMapping
    public String listNotifications(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login";
        }
        List<Notification> notifications = notificationService.findForUser(user.getId());
        model.addAttribute("notifications", notifications);
        return "notification/list";
    }

    @GetMapping("/{id}")
    public String viewNotification(@PathVariable Long id,
                                 HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }
            Notification notification = notificationService.markRead(id, user.getId());
            model.addAttribute("notification", notification);
            return "notification/view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/notifications";
        }
    }

    @PostMapping("/{id}/read")
    public String markRead(@PathVariable Long id,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        return updateReadState(id, true, session, redirectAttributes);
    }

    @PostMapping("/{id}/unread")
    public String markUnread(@PathVariable Long id,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        return updateReadState(id, false, session, redirectAttributes);
    }

    @PostMapping("/{id}/delete")
    public String deleteNotification(@PathVariable Long id,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }
            notificationService.delete(id, user.getId());
            redirectAttributes.addFlashAttribute("success", "Notification deleted");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete notification: " + e.getMessage());
        }
        return "redirect:/notifications";
    }

    @GetMapping("/templates")
    public String listTemplates(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (!isAdmin(user)) {
            return "redirect:/users/login";
        }
        model.addAttribute("templates", notificationTemplateService.findAll());
        return "notification/templates";
    }

    @GetMapping("/templates/new")
    public String newTemplate(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (!isAdmin(user)) {
            return "redirect:/users/login";
        }
        model.addAttribute("template", new NotificationTemplate());
        addTemplateFormAttributes(model, "/lankajobshub/notifications/templates");
        return "notification/template-form";
    }

    @PostMapping("/templates")
    public String createTemplate(@ModelAttribute NotificationTemplate template,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (!isAdmin(user)) {
            return "redirect:/users/login";
        }
        try {
            notificationTemplateService.save(template, user);
            redirectAttributes.addFlashAttribute("success", "Template created");
            return "redirect:/notifications/templates";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create template: " + e.getMessage());
            return "redirect:/notifications/templates/new";
        }
    }

    @GetMapping("/templates/{id}/edit")
    public String editTemplate(@PathVariable Long id,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (!isAdmin(user)) {
            return "redirect:/users/login";
        }
        try {
            model.addAttribute("template", notificationTemplateService.findById(id));
            addTemplateFormAttributes(model, "/lankajobshub/notifications/templates/" + id + "/edit");
            return "notification/template-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/notifications/templates";
        }
    }

    @PostMapping("/templates/{id}/edit")
    public String updateTemplate(@PathVariable Long id,
                                 @ModelAttribute NotificationTemplate template,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (!isAdmin(user)) {
            return "redirect:/users/login";
        }
        try {
            template.setId(id);
            notificationTemplateService.save(template, user);
            redirectAttributes.addFlashAttribute("success", "Template updated");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update template: " + e.getMessage());
            return "redirect:/notifications/templates/" + id + "/edit";
        }
        return "redirect:/notifications/templates";
    }

    @PostMapping("/templates/{id}/delete")
    public String deleteTemplate(@PathVariable Long id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (!isAdmin(user)) {
            return "redirect:/users/login";
        }
        try {
            notificationTemplateService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Template deleted");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete template: " + e.getMessage());
        }
        return "redirect:/notifications/templates";
    }

    private String updateReadState(Long id, boolean read, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }
            if (read) {
                notificationService.markRead(id, user.getId());
            } else {
                notificationService.markUnread(id, user.getId());
            }
            redirectAttributes.addFlashAttribute("success", "Notification updated");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update notification: " + e.getMessage());
        }
        return "redirect:/notifications";
    }

    private void addTemplateFormAttributes(Model model, String formAction) {
        model.addAttribute("formAction", formAction);
        model.addAttribute("types", Notification.NotificationType.values());
        model.addAttribute("statuses", NotificationTemplate.TemplateStatus.values());
    }

    private boolean isAdmin(User user) {
        return user != null && user.getRole() == UserRole.ADMIN;
    }
}
