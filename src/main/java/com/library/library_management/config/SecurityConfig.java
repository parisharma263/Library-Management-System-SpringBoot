package com.library.library_management.config;

import com.library.library_management.handler.CustomAuthenticationSuccessHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        // Publicly accessible paths, including all static files
//                        .requestMatchers("/", "/signup", "/login", "/static/**", "/css/**").permitAll()

                                .requestMatchers("/", "/signup", "/login", "/style.css", "/static/**", "/css/**").permitAll()
                        // ADMIN-only pages (e.g., managing students, editing books, issuing books is an admin function)
                        .requestMatchers("/admin/**", "/students/edit/**", "/students/delete/**", "/books/edit/**", "/issue_book/**").hasRole("ADMIN")

                        // Pages accessible to BOTH (e.g., viewing all books)
                        .requestMatchers("/books", "/books/{id}").hasAnyRole("ADMIN", "STUDENT")

                        // STUDENT-only pages (e.g., viewing their dashboard and issued books)
                        .requestMatchers("/student/dashboard", "/issued-books", "/issued-books/**", "/books/request/**").hasRole("STUDENT")

                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        // Use the custom handler for role-based redirect
                        .successHandler(customAuthenticationSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    // 1. Bean for the Custom Success Handler
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    // 2. Bean for Password Encoding
    // Your teacher moved this here, making PasswordConfig unnecessary.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 3. Bean for Authentication Provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // Note: I removed the PasswordEncoder argument from the method signature
        // to match your current implementation style where it calls the bean directly.
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}