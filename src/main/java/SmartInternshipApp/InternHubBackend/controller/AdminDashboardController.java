package SmartInternshipApp.InternHubBackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin(originPatterns = "*")
public class AdminDashboardController {
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", 0);
        stats.put("totalInternships", 0);
        stats.put("totalCompanies", 0);
        stats.put("totalApplications", 0);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/college-stats")
    public ResponseEntity<List<Map<String, Object>>> getCollegeWiseStats() {
        return ResponseEntity.ok(new ArrayList<>());
    }
}