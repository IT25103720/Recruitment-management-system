package com.lankajobshub.controller;

import com.lankajobshub.model.Company;
import com.lankajobshub.model.User;
import com.lankajobshub.model.UserRole;
import com.lankajobshub.repository.CompanyRepository;
import com.lankajobshub.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobRepository jobRepository;

    @GetMapping("/{id}")
    public String companyProfile(@PathVariable Long id, Model model) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) {
            return "redirect:/jobs";
        }
        model.addAttribute("company", company);
        model.addAttribute("jobs", jobRepository.findByCompanyId(id));
        return "company/profile";
    }

    @GetMapping
    public String listCompanies(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (!canManageCompanies(user)) {
            return "redirect:/users/login";
        }
        model.addAttribute("companies", companyRepository.findAll());
        model.addAttribute("company", new Company());
        return "company/manage";
    }

    @PostMapping("/create")
    public String createCompany(@ModelAttribute Company company,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (!canManageCompanies(user)) {
            return "redirect:/users/login";
        }
        try {
            if (company.getName() == null || company.getName().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Company name is required");
                return "redirect:/companies";
            }
            if (companyRepository.existsByName(company.getName())) {
                redirectAttributes.addFlashAttribute("error", "A company with this name already exists");
                return "redirect:/companies";
            }
            companyRepository.save(company);
            redirectAttributes.addFlashAttribute("success", "Company created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create company: " + e.getMessage());
        }
        return "redirect:/companies";
    }

    @PostMapping("/{id}/delete")
    public String deleteCompany(@PathVariable Long id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (!canManageCompanies(user)) {
            return "redirect:/users/login";
        }
        try {
            if (companyRepository.existsById(id)) {
                companyRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Company deleted successfully");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete company: " + e.getMessage());
        }
        return "redirect:/companies";
    }

    private boolean canManageCompanies(User user) {
        return user != null
                && (user.getRole() == UserRole.ADMIN
                || user.getRole() == UserRole.CLIENT_RELATIONS_EXECUTIVE);
    }
}

