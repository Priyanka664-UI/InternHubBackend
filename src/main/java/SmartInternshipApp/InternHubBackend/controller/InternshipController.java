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
    
    @GetMapping("/by-location")
    public ResponseEntity<List<Internship>> getInternshipsByLocation(
            @RequestParam(required = false) Long stateId,
            @RequestParam(required = false) Long cityId) {
        return ResponseEntity.ok(internshipService.getInternshipsByLocation(stateId, cityId));
    }
    
    @GetMapping("/by-state/{stateId}")
    public ResponseEntity<List<Internship>> getInternshipsByState(@PathVariable Long stateId) {
        return ResponseEntity.ok(internshipService.getInternshipsByState(stateId));
    }
    
    @GetMapping("/by-city/{cityId}")
    public ResponseEntity<List<Internship>> getInternshipsByCity(@PathVariable Long cityId) {
        return ResponseEntity.ok(internshipService.getInternshipsByCity(cityId));
    }
    
    @GetMapping("/by-work-type/{workType}")
    public ResponseEntity<List<Internship>> getInternshipsByWorkType(@PathVariable String workType) {
        return ResponseEntity.ok(internshipService.getInternshipsByWorkType(workType));
    }
    
    @PostMapping
    public ResponseEntity<Internship> createInternship(@RequestBody Internship internship) {
        return ResponseEntity.ok(internshipService.createInternship(internship));
    }
}