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
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "applicationType", required = false) String applicationType,
            @RequestParam(value = "teamSize", required = false) Integer teamSize,
            @RequestParam(value = "teamLeader", required = false) String teamLeader,
            @RequestParam(value = "leaderContact", required = false) String leaderContact,
            @RequestParam(value = "leaderEmail", required = false) String leaderEmail,
            @RequestParam(value = "teamMembers", required = false) String teamMembers,
            @RequestParam(value = "memberEmails", required = false) String memberEmails,
            @RequestParam(value = "academicYear", required = false) String academicYear,
            @RequestParam(value = "semester", required = false) String semester,
            @RequestParam(value = "skills", required = false) String skills,
            @RequestParam(value = "experience", required = false) String experience,
            @RequestParam(value = "motivation", required = false) String motivation,
            @RequestParam(value = "paymentStatus", required = false) String paymentStatus,
            @RequestParam(value = "paymentAmount", required = false) Double paymentAmount,
            @RequestParam(value = "paymentId", required = false) String paymentId,
            @RequestParam(value = "studentId", required = false) MultipartFile studentIdFile,
            @RequestParam(value = "resume", required = false) MultipartFile resumeFile,
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        try {
            System.out.println("=== Application Submission Started ===");
            System.out.println("Received application: internshipId=" + internshipId);
            System.out.println("College: " + college + ", Degree: " + degree + ", Year: " + yearOfStudy);
            System.out.println("GroupId: " + groupId + ", ApplicationType: " + applicationType);
            System.out.println("Payment Status: " + paymentStatus + ", Payment Amount: " + paymentAmount + ", Payment ID: " + paymentId);
            if ("GROUP".equals(applicationType)) {
                System.out.println("Team Size: " + teamSize + ", Team Leader: " + teamLeader);
                System.out.println("Leader Contact: " + leaderContact + ", Leader Email: " + leaderEmail);
            }
            System.out.println("Token: " + (token != null ? "Present" : "Missing"));
            System.out.println("StudentId file: " + (studentIdFile != null ? studentIdFile.getOriginalFilename() : "null"));
            System.out.println("Resume file: " + (resumeFile != null ? resumeFile.getOriginalFilename() : "null"));
            
            applicationService.submitApplication(internshipId, college, degree, yearOfStudy, 
                    groupId, applicationType, teamSize, teamLeader, leaderContact, leaderEmail,
                    teamMembers, memberEmails, academicYear, semester, skills, experience, 
                    motivation, paymentStatus, paymentAmount, paymentId, studentIdFile, resumeFile, token);
            
            System.out.println("=== Application Submitted Successfully ===");
            return ResponseEntity.ok().body("{\"message\": \"Application submitted successfully\"}");
        } catch (Exception e) {
            System.err.println("=== Application Submission Failed ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
