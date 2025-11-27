package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.service.AdminDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin(origins = "*")
public class AdminDashboardController {
    
    @Autowired
    private AdminDashboardService dashboardService;
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }
    
    @GetMapping("/college-stats")
    public ResponseEntity<List<Map<String, Object>>> getCollegeWiseStats() {
        return ResponseEntity.ok(dashboardService.getCollegeWiseStats());
    }
}