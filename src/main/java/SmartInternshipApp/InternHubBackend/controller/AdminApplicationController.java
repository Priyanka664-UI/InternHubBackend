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
    
    @GetMapping
    public ResponseEntity<List<InternshipApplication>> getAllApplications() {
        return ResponseEntity.ok(applicationRepository.findAll());
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
        
        Map<String, Object> response = new HashMap<>();
        response.put("application", application);
        
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