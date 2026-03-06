package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.service.IndividualApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/applications/individual")
public class IndividualApplicationController {

    @Autowired
    private IndividualApplicationService applicationService;

    @PostMapping
    public ResponseEntity<?> submitIndividualApplication(
            @RequestParam("internshipId") Long internshipId,
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("duration") String duration,
            @RequestParam(value = "skills", required = false) String skills,
            @RequestParam("motivation") String motivation,
            @RequestParam("college") String college,
            @RequestParam("degree") String degree,
            @RequestParam("yearOfStudy") String yearOfStudy,
            @RequestParam(value = "paymentStatus", required = false) String paymentStatus,
            @RequestParam(value = "paymentAmount", required = false) Double paymentAmount,
            @RequestParam(value = "paymentId", required = false) String paymentId,
            @RequestParam("resume") MultipartFile resumeFile,
            @RequestParam("pastQualification") MultipartFile pastQualificationFile,
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        try {
            Long applicationId = applicationService.submitIndividualApplication(
                internshipId, fullName, email, phone, duration, skills, motivation,
                college, degree, yearOfStudy, paymentStatus, paymentAmount, paymentId,
                resumeFile, pastQualificationFile, token
            );
            
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("message", "Application submitted successfully");
            response.put("id", applicationId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
