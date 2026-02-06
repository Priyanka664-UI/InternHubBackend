package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.InternshipApplication;
import SmartInternshipApp.InternHubBackend.entity.InternshipApplication.ApplicationStatus;
import SmartInternshipApp.InternHubBackend.repository.InternshipApplicationRepository;
import SmartInternshipApp.InternHubBackend.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/admin/applications")
public class AdminApplicationController {
    
    @Autowired
    private InternshipApplicationRepository applicationRepository;
    
    @Autowired
    private CertificateService certificateService;
    
    @Autowired
    private SmartInternshipApp.InternHubBackend.service.NotificationService notificationService;
    
    @GetMapping
    public ResponseEntity<List<InternshipApplication>> getAllApplications(
            @RequestHeader(value = "X-User-Type", required = false) Integer userType,
            @RequestHeader(value = "X-Company-Id", required = false) Long companyId) {
        
        System.out.println("=== GET APPLICATIONS ===");
        System.out.println("User Type: " + userType);
        System.out.println("Company ID: " + companyId);
        
        List<InternshipApplication> applications = applicationRepository.findAll();
        System.out.println("Total applications before filter: " + applications.size());
        
        // Filter by company if user_type = 2
        if (userType != null && userType == 2 && companyId != null) {
            applications = applications.stream()
                .filter(app -> {
                    boolean match = app.getInternship() != null && 
                                   companyId.equals(app.getInternship().getCompanyId());
                    System.out.println("Application " + app.getId() + ": internship companyId=" + 
                        (app.getInternship() != null ? app.getInternship().getCompanyId() : "null") + 
                        ", match=" + match);
                    return match;
                })
                .collect(java.util.stream.Collectors.toList());
            System.out.println("Applications after filter: " + applications.size());
        }
        
        return ResponseEntity.ok(applications);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<InternshipApplication>> getApplicationsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(applicationRepository.findByStatus(ApplicationStatus.valueOf(status)));
    }
    
    @GetMapping("/internship/{internshipId}")
    public ResponseEntity<List<InternshipApplication>> getApplicationsByInternship(@PathVariable Long internshipId) {
        return ResponseEntity.ok(applicationRepository.findByInternshipId(internshipId));
    }
    
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<Map<String, Object>> updateApplicationStatus(
            @PathVariable Long applicationId, 
            @RequestBody Map<String, String> request) {
        InternshipApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        String newStatus = request.get("status");
        application.setStatus(ApplicationStatus.valueOf(newStatus));
        applicationRepository.save(application);
        
        String internshipTitle = application.getInternship().getTitle();
        Long studentId = application.getStudent().getId();
        
        if ("ACCEPTED".equals(newStatus)) {
            notificationService.createNotification(studentId, "Application Accepted", 
                "Congratulations! Your application for " + internshipTitle + " has been accepted.", "SUCCESS");
        } else if ("REJECTED".equals(newStatus)) {
            notificationService.createNotification(studentId, "Application Rejected", 
                "Your application for " + internshipTitle + " has been rejected.", "ERROR");
        } else if ("COMPLETED".equals(newStatus)) {
            notificationService.createNotification(studentId, "Internship Completed", 
                "You have successfully completed the internship: " + internshipTitle, "SUCCESS");
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("application", application);
        response.put("notificationSent", true);
        
        if ("COMPLETED".equals(newStatus)) {
            try {
                var certificate = certificateService.issueCertificate(
                    application.getStudent().getId(),
                    application.getInternship().getId(),
                    5,
                    "Successfully completed internship"
                );
                response.put("certificateGenerated", true);
                response.put("certificateNumber", certificate.getCertificateNumber());
            } catch (Exception e) {
                response.put("certificateGenerated", false);
            }
        }
        
        return ResponseEntity.ok(response);
    }
}