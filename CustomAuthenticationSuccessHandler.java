package com.lankajobshub.config;

import com.lankajobshub.config.CustomUserDetails;
import com.lankajobshub.model.User;
import com.lankajobshub.model.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();
        System.out.println("Authenticated user name (from authentication.getName()): " + authentication.getName());

        // Store the authenticated domain user in session for controllers that rely on it
        request.getSession().setAttribute("user", user);

        String redirect = request.getParameter("redirect");
        if (redirect != null && redirect.startsWith("/applications/apply/") && user.getRole() == UserRole.JOB_SEEKER) {
            response.sendRedirect(request.getContextPath() + redirect);
            return;
        }

        DefaultSavedRequest savedRequest = (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
        if (savedRequest != null && user.getRole() == UserRole.JOB_SEEKER && savedRequest.getRedirectUrl().contains("/applications/apply/")) {
            response.sendRedirect(savedRequest.getRedirectUrl());
        } else if (user.getRole() == UserRole.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/analytics/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
}
