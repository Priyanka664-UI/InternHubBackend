package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.HelpSupport;
import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.repository.HelpSupportRepository;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/help-support")
@CrossOrigin(origins = "*")
public class HelpSupportController {
    
    @Autowired
    private HelpSupportRepository helpSupportRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody HelpSupportRequest request) {
        try {
            Student student = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            
            HelpSupport helpSupport = new HelpSupport();
            helpSupport.setStudent(student);
            helpSupport.setSubject(request.getSubject());
            helpSupport.setMessage(request.getMessage());
            
            HelpSupport saved = helpSupportRepository.save(helpSupport);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<List<HelpSupport>> getAllRequests() {
        return ResponseEntity.ok(helpSupportRepository.findAllByOrderByCreatedAtDesc());
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<HelpSupport>> getStudentRequests(@PathVariable Long studentId) {
        return ResponseEntity.ok(helpSupportRepository.findByStudentIdOrderByCreatedAtDesc(studentId));
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            HelpSupport helpSupport = helpSupportRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Request not found"));
            helpSupport.setStatus(status);
            return ResponseEntity.ok(helpSupportRepository.save(helpSupport));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}/response")
    public ResponseEntity<?> addResponse(@PathVariable Long id, @RequestBody ResponseRequest request) {
        try {
            HelpSupport helpSupport = helpSupportRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Request not found"));
            helpSupport.setAdminResponse(request.getResponse());
            helpSupport.setStatus("RESOLVED");
            return ResponseEntity.ok(helpSupportRepository.save(helpSupport));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // DTOs
    public static class HelpSupportRequest {
        private Long studentId;
        private String subject;
        private String message;
        
        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class ResponseRequest {
        private String response;
        
        public String getResponse() { return response; }
        public void setResponse(String response) { this.response = response; }
    }
}
