package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.service.NotificationService;
import SmartInternshipApp.InternHubBackend.service.StudentService;
import SmartInternshipApp.InternHubBackend.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/notifications")
public class AdminNotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private StudentService studentService;
    
    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcastNotification(@RequestBody Map<String, String> request) {
        String title = request.get("title");
        String message = request.get("message");
        String type = request.get("type");
        
        List<Student> students = studentService.getAllStudents();
        for (Student student : students) {
            notificationService.createNotification(student.getId(), title, message, type);
        }
        
        return ResponseEntity.ok("Notification broadcasted to " + students.size() + " students");
    }
}