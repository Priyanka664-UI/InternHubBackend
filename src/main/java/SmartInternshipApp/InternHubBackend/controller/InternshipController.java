package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Internship;
import SmartInternshipApp.InternHubBackend.service.InternshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/internships")
public class InternshipController {
    
    @Autowired
    private InternshipService internshipService;
    
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Internship>> getInternshipsByCompany(
            @PathVariable Long companyId,
            @RequestParam(required = false) String companyName) {
        return ResponseEntity.ok(internshipService.getInternshipsByCompany(companyId, companyName));
    }
}
