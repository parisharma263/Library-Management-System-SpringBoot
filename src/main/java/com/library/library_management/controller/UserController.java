package com.library.library_management.controller;

import com.library.library_management.model.Student;
import com.library.library_management.model.User;
import com.library.library_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Show the login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Show the signup page
    // UserController.java mein
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        User user = new User();
        // User ke andar ek naya Student object daalein
        user.setStudent(new Student());
        model.addAttribute("user", user);
        return "signup";
    }

    // Handle signup form submission
    @PostMapping("/signup")
    public String registerUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/login";
    }
}