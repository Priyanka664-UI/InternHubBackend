package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.service.StudentService;
import SmartInternshipApp.InternHubBackend.dto.PasswordChangeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class StudentProfileController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/{id}")
    public ResponseEntity<Student> getProfile(@PathVariable Long id) {
        return studentService.getProfile(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateProfile(@PathVariable Long id, @RequestBody Student student) {
        Student updated = studentService.updateProfile(id, student);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfile(@PathVariable Long id) {
        boolean deleted = studentService.deleteProfile(id);
        return deleted ? ResponseEntity.ok("Profile deleted successfully") : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/password")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody PasswordChangeRequest request) {
        boolean changed = studentService.changePassword(id, request);
        return changed ? ResponseEntity.ok("Password changed successfully") : ResponseEntity.badRequest().body("Invalid current password");
    }
}