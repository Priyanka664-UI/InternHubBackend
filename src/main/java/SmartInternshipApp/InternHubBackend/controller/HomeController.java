package SmartInternshipApp.InternHubBackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "Welcome to InternHub - Smart Internship Portal For Colleges";
    }
    
    @GetMapping("/status")
    public String status() {
        return "Application is running and database is connected!";
    }
}