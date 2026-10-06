package com.lankajobshub.controller;

import com.lankajobshub.model.User;
import com.lankajobshub.model.UserRole;
import com.lankajobshub.service.CandidateProfileService;
import com.lankajobshub.service.FileUploadService;
import com.lankajobshub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/upload")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private UserService userService;

    @Autowired
    private CandidateProfileService candidateProfileService;

    @PostMapping("/resume")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadResume(
            @RequestParam("file") MultipartFile file,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.put("success", false);
                response.put("message", "User not authenticated");
                return ResponseEntity.badRequest().body(response);
            }

            // Validate file type
            String[] allowedTypes = {".pdf", ".doc", ".docx"};
            if (!fileUploadService.isValidFileType(file, allowedTypes)) {
                response.put("success", false);
                response.put("message", "Invalid file type. Only PDF, DOC, and DOCX files are allowed.");
                return ResponseEntity.badRequest().body(response);
            }

            String filePath = fileUploadService.uploadResume(file, user.getId());

            // Persist to user and refresh session
            User saved = userService.updateResumePath(user.getId(), filePath);
            if (saved.getRole() == UserRole.JOB_SEEKER) {
                candidateProfileService.updateResumePath(saved.getId(), filePath);
            }
            session.setAttribute("user", saved);
            
            response.put("success", true);
            response.put("message", "Resume uploaded successfully");
            response.put("filePath", filePath);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Error uploading file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/resume/delete")
    public String deleteResume(HttpSession session,
                             org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/users/login";
            }
            if (user.getResumePath() != null) {
                fileUploadService.deleteFile(user.getResumePath());
            }
            User saved = userService.updateResumePath(user.getId(), null);
            if (saved.getRole() == UserRole.JOB_SEEKER) {
                candidateProfileService.updateResumePath(saved.getId(), null);
            }
            session.setAttribute("user", saved);
            redirectAttributes.addFlashAttribute("success", "Resume deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete resume: " + e.getMessage());
        }
        return "redirect:/users/profile/edit";
    }

    @PostMapping("/profile-photo")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadProfilePhoto(
            @RequestParam("file") MultipartFile file,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.put("success", false);
                response.put("message", "User not authenticated");
                return ResponseEntity.badRequest().body(response);
            }

            // Validate file type
            String[] allowedTypes = {".jpg", ".jpeg", ".png", ".gif"};
            if (!fileUploadService.isValidFileType(file, allowedTypes)) {
                response.put("success", false);
                response.put("message", "Invalid file type. Only JPG, JPEG, PNG, and GIF files are allowed.");
                return ResponseEntity.badRequest().body(response);
            }

            String filePath = fileUploadService.uploadProfilePhoto(file, user.getId());

            // Persist to user and refresh session
            User saved = userService.updateProfilePhotoPath(user.getId(), filePath);
            session.setAttribute("user", saved);
            
            response.put("success", true);
            response.put("message", "Profile photo uploaded successfully");
            response.put("filePath", filePath);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Error uploading file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/file")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteFile(
            @RequestParam("filePath") String filePath,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "User not authenticated");
            return ResponseEntity.badRequest().body(response);
        }

        fileUploadService.deleteFile(filePath);
        
        response.put("success", true);
        response.put("message", "File deleted successfully");
        
        return ResponseEntity.ok(response);
    }
}
