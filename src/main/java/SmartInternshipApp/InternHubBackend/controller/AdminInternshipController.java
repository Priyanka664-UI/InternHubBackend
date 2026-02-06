package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Internship;
import SmartInternshipApp.InternHubBackend.service.InternshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/internships")
public class AdminInternshipController {
    
    @Autowired
    private InternshipService internshipService;
    
    @GetMapping
    public ResponseEntity<List<Internship>> getAllInternships(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String companyName,
            @RequestHeader(value = "X-User-Type", required = false) Integer userType,
            @RequestHeader(value = "X-Company-Id", required = false) Long loggedInCompanyId) {
        
        // If user_type = 2 (company), filter by their company ID
        if (userType != null && userType == 2 && loggedInCompanyId != null) {
            return ResponseEntity.ok(internshipService.getInternshipsByCompany(loggedInCompanyId, null));
        }
        
        // Main admin sees all or filtered data
        if (companyId != null || companyName != null) {
            return ResponseEntity.ok(internshipService.getInternshipsByCompany(companyId, companyName));
        }
        return ResponseEntity.ok(internshipService.getAllInternships());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Internship> getInternshipById(@PathVariable Long id) {
        Optional<Internship> internship = internshipService.getInternshipById(id);
        return internship.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Internship> createInternship(
            @RequestBody Internship internship,
            @RequestHeader(value = "X-User-Type", required = false) Integer userType,
            @RequestHeader(value = "X-Company-Id", required = false) Long companyId) {
        
        // If company user, automatically set companyId
        if (userType != null && userType == 2 && companyId != null) {
            internship.setCompanyId(companyId);
        }
        
        return ResponseEntity.ok(internshipService.createInternship(internship));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Internship> updateInternship(@PathVariable Long id, @RequestBody Internship internship) {
        Internship updated = internshipService.updateInternship(id, internship);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInternship(@PathVariable Long id) {
        try {
            boolean deleted = internshipService.deleteInternship(id);
            return deleted ? ResponseEntity.ok("Internship deleted successfully") : ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Internship>> searchInternships(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(internshipService.searchInternships(keyword));
    }
}