package SmartInternshipApp.InternHubBackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/admin/applications")
public class AdminApplicationController {
    
    @GetMapping
    public ResponseEntity<List<Object>> getAllApplications() {
        return ResponseEntity.ok(new ArrayList<>());
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Object>> getApplicationsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(new ArrayList<>());
    }
    
    @GetMapping("/internship/{internshipId}")
    public ResponseEntity<List<Object>> getApplicationsByInternship(@PathVariable Long internshipId) {
        return ResponseEntity.ok(new ArrayList<>());
    }
}