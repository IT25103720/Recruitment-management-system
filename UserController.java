package com.lankajobshub.controller;

import com.lankajobshub.model.CandidateProfile;
import com.lankajobshub.model.JobType;
import com.lankajobshub.model.User;
import com.lankajobshub.model.UserRole;
import com.lankajobshub.service.CandidateProfileService;
import com.lankajobshub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CandidateProfileService candidateProfileService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        // Only allow JOB_SEEKER registration for public users
        model.addAttribute("roles", new UserRole[]{UserRole.JOB_SEEKER});
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, 
                              @RequestParam String confirmPassword,
                              RedirectAttributes redirectAttributes) {
        try {
            // Basic validation
            if (!user.getPassword().equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match");
                return "redirect:/users/register";
            }

            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Email is required");
                return "redirect:/users/register";
            }

            // Enforce that only JOB_SEEKER can self-register
            if (user.getRole() != UserRole.JOB_SEEKER) {
                redirectAttributes.addFlashAttribute("error", "Only job seekers can register publicly. Other roles must be created by authorized users.");
                return "redirect:/users/register";
            }

            User registeredUser = userService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/users/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/users/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model,
                                jakarta.servlet.http.HttpServletRequest request,
                                @RequestParam(required = false) String redirect) {
        if (redirect != null && redirect.startsWith("/applications/apply/")) {
            model.addAttribute("redirect", redirect);
        }
        String errorParam = request.getParameter("error");
        if (errorParam != null) {
            Object lastEx = request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
            String message = "Login failed. Please check your credentials and try again.";
            if (lastEx instanceof BadCredentialsException) {
                message = "Incorrect email or password.";
            } else if (lastEx instanceof DisabledException) {
                message = "Your account is disabled. Please contact support.";
            } else if (lastEx instanceof LockedException) {
                message = "Your account is locked. Please contact support.";
            } else if (lastEx instanceof Exception) {
                message = ((Exception) lastEx).getMessage();
            }
            model.addAttribute("error", message);
            request.getSession().removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        }
        return "user/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, 
                           @RequestParam String password,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        try {
            if (userService.authenticateUser(email, password)) {
                User authenticatedUser = userService.findByEmail(email).orElse(null);
                if (authenticatedUser != null) {
                    session.setAttribute("user", authenticatedUser);
                    userService.updateLastLogin(authenticatedUser.getId());
                }
                redirectAttributes.addFlashAttribute("success", "Login successful!");
                if (authenticatedUser != null && authenticatedUser.getRole() == UserRole.ADMIN) {
                    return "redirect:/users/admin";
                } else {
                    return "redirect:/dashboard";
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid email or password");
                return "redirect:/users/login";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Login failed: " + e.getMessage());
            return "redirect:/users/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "Logged out successfully");
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login";
        }
        model.addAttribute("user", user);
        if (user.getRole() == UserRole.JOB_SEEKER) {
            model.addAttribute("candidateProfile", candidateProfileService.findByUserId(user.getId()).orElse(null));
        }
        return "user/profile";
    }

    @GetMapping("/profile/edit")
    public String showEditProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("jobTypes", JobType.values());
        if (user.getRole() == UserRole.JOB_SEEKER) {
            model.addAttribute("candidateProfile", candidateProfileService.findByUserId(user.getId()).orElse(new CandidateProfile()));
        }
        return "user/edit-profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute User user, 
                               @ModelAttribute CandidateProfile candidateProfile,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return "redirect:/users/login";
            }

            user.setId(currentUser.getId());
            user.setEmail(currentUser.getEmail());
            user.setPassword(currentUser.getPassword()); // Keep existing password
            user.setRole(currentUser.getRole());
            user.setStatus(currentUser.getStatus());
            User updatedUser = userService.updateUser(user);
            if (updatedUser.getRole() == UserRole.JOB_SEEKER) {
                candidateProfileService.saveForUser(updatedUser.getId(), candidateProfile);
            }
            session.setAttribute("user", updatedUser);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully");
            return "redirect:/users/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile: " + e.getMessage());
            return "redirect:/users/profile/edit";
        }
    }

    @PostMapping("/profile/candidate/delete")
    public String deleteCandidateProfile(HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return "redirect:/users/login";
            }
            candidateProfileService.deleteForUser(currentUser.getId());
            redirectAttributes.addFlashAttribute("success", "Candidate profile data deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete candidate profile: " + e.getMessage());
        }
        return "redirect:/users/profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return "redirect:/users/login";
            }
            if (newPassword == null || newPassword.length() < 6) {
                redirectAttributes.addFlashAttribute("error", "New password must be at least 6 characters");
                return "redirect:/users/profile/edit";
            }
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "New passwords do not match");
                return "redirect:/users/profile/edit";
            }

            User updatedUser = userService.changePassword(currentUser.getId(), currentPassword, newPassword);
            session.setAttribute("user", updatedUser);
            redirectAttributes.addFlashAttribute("success", "Password updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update password: " + e.getMessage());
        }
        return "redirect:/users/profile/edit";
    }

    @PostMapping("/profile/delete")
    public String deleteOwnAccount(HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/users/login";
        }

        try {
            userService.deleteUser(currentUser.getId());
            session.invalidate();
            redirectAttributes.addFlashAttribute("success", "Your account has been deleted. You can register again anytime.");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete account: " + e.getMessage());
            return "redirect:/users/profile";
        }
    }

    @GetMapping("/admin")
    public String showAdminPanel(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != UserRole.ADMIN) {
            return "redirect:/users/login";
        }
        
        List<User> allUsers = userService.findAll();
        model.addAttribute("users", allUsers);
        return "admin/users";
    }

    @PostMapping("/admin/{id}/delete")
    public String deleteUser(@PathVariable Long id, 
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getRole() != UserRole.ADMIN) {
            return "redirect:/users/login";
        }

        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user: " + e.getMessage());
        }
        return "redirect:/users/admin";
    }

    // New methods for hierarchical user creation
    @GetMapping("/create")
    public String showCreateUserForm(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/users/login";
        }

        // Check if user has permission to create other users
        UserRole[] creatableRoles = currentUser.getRole().getCreatableRoles();
        if (creatableRoles.length == 0) {
            redirectAttributes.addFlashAttribute("error", "You don't have permission to create user accounts.");
            return "redirect:/dashboard";
        }

        model.addAttribute("user", new User());
        model.addAttribute("creatableRoles", creatableRoles);
        model.addAttribute("currentUser", currentUser);
        return "user/create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute User user,
                            @RequestParam String confirmPassword,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/users/login";
        }

        try {
            // Validate that current user can create the target role
            if (!currentUser.getRole().canCreateRole(user.getRole())) {
                redirectAttributes.addFlashAttribute("error", "You don't have permission to create users with this role.");
                return "redirect:/users/create";
            }

            // Basic validation
            if (!user.getPassword().equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match");
                return "redirect:/users/create";
            }

            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Email is required");
                return "redirect:/users/create";
            }

            User createdUser = userService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "User account created successfully!");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/users/create";
        }
    }
}
