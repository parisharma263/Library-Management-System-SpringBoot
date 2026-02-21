package com.library.library_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // Jab bhi koi root URL khole, usse /login par bhej do
        return "index";
    }
}