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
@CrossOrigin(origins = "*")
public class AdminInternshipController {
    
    @Autowired
    private InternshipService internshipService;
    
    @GetMapping
    public ResponseEntity<List<Internship>> getAllInternships() {
        return ResponseEntity.ok(internshipService.getAllInternships());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Internship> getInternshipById(@PathVariable Long id) {
        Optional<Internship> internship = internshipService.getInternshipById(id);
        return internship.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Internship> createInternship(@RequestBody Internship internship) {
        return ResponseEntity.ok(internshipService.createInternship(internship));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Internship> updateInternship(@PathVariable Long id, @RequestBody Internship internship) {
        Internship updated = internshipService.updateInternship(id, internship);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInternship(@PathVariable Long id) {
        boolean deleted = internshipService.deleteInternship(id);
        return deleted ? ResponseEntity.ok("Internship deleted successfully") : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Internship>> searchInternships(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(internshipService.searchInternships(keyword));
    }
}