package SmartInternshipApp.InternHubBackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestStudentController {
    
    @PostMapping("/students")
    public ResponseEntity<Map<String, Object>> testCreateStudent(@RequestBody Map<String, Object> studentData) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Test endpoint working - Student data received");
        response.put("receivedData", studentData);
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/students")
    public ResponseEntity<Map<String, Object>> testGetStudents() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Test GET endpoint working");
        response.put("data", "No actual data - this is a test endpoint");
        return ResponseEntity.ok(response);
    }
}