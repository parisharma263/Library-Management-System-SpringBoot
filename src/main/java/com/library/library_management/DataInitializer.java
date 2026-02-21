package com.library.library_management;

import com.library.library_management.model.User;
import com.library.library_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Check if admin user already exists
        if (userService.findByUsername("admin") == null) {
            // Create a new user object for Admin
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword("adminpass"); // Aap password kuch bhi rakh sakte hain
            adminUser.setRole(User.Role.ADMIN);

            // Save the admin user
            userService.saveUser(adminUser);
            System.out.println(">>>>>>>> Admin user created successfully! <<<<<<<<");
        }
    }
}