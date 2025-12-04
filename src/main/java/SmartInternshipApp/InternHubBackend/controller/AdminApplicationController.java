package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.InternshipApplication;
import SmartInternshipApp.InternHubBackend.entity.InternshipApplication.ApplicationStatus;
import SmartInternshipApp.InternHubBackend.repository.InternshipApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/applications")
public class AdminApplicationController {
    
    @Autowired
    private InternshipApplicationRepository applicationRepository;
    
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
    public ResponseEntity<InternshipApplication> updateApplicationStatus(
            @PathVariable Long applicationId, 
            @RequestBody Map<String, String> request) {
        InternshipApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(ApplicationStatus.valueOf(request.get("status")));
        return ResponseEntity.ok(applicationRepository.save(application));
    }
}