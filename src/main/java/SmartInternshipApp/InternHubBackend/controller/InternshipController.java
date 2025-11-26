package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Internship;
import SmartInternshipApp.InternHubBackend.service.InternshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/internships")
@CrossOrigin(origins = "*")
public class InternshipController {
    
    @Autowired
    private InternshipService internshipService;
    
    @GetMapping
    public ResponseEntity<List<Internship>> getAllInternships() {
        return ResponseEntity.ok(internshipService.getAllInternships());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Internship>> searchInternships(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(internshipService.searchInternships(keyword));
    }
    
    @PostMapping
    public ResponseEntity<Internship> createInternship(@RequestBody Internship internship) {
        return ResponseEntity.ok(internshipService.createInternship(internship));
    }
}