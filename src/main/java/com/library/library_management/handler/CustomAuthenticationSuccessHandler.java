package com.library.library_management.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;
import java.util.Collection;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "/"; // Default fallback

        // Check for the ADMIN role
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            redirectUrl = "/admin/dashboard";
        }
        // Check for the STUDENT role
        else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"))) {
            redirectUrl = "/student/dashboard";
        }

        response.sendRedirect(redirectUrl);
    }
}