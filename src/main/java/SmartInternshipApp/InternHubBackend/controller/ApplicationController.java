package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<?> submitApplication(
            @RequestParam("internshipId") Long internshipId,
            @RequestParam("college") String college,
            @RequestParam("degree") String degree,
            @RequestParam("yearOfStudy") String yearOfStudy,
            @RequestParam(value = "studentId", required = false) MultipartFile studentIdFile,
            @RequestParam(value = "resume", required = false) MultipartFile resumeFile,
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        try {
            System.out.println("Received application: internshipId=" + internshipId);
            System.out.println("College: " + college + ", Degree: " + degree + ", Year: " + yearOfStudy);
            System.out.println("Token: " + (token != null ? "Present" : "Missing"));
            
            applicationService.submitApplication(internshipId, college, degree, yearOfStudy, 
                    studentIdFile, resumeFile, token);
            return ResponseEntity.ok().body("{\"message\": \"Application submitted successfully\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
