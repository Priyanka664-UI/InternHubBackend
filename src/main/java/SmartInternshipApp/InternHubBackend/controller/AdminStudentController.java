package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/students")
public class AdminStudentController {
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }
}