package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.dto.RegistrationRequest;
import SmartInternshipApp.InternHubBackend.dto.LoginRequest;
import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AuthService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    public String register(RegistrationRequest request) {
        try {
            if (studentRepository.existsByEmail(request.getEmail())) {
                return "Email already exists";
            }
            
            Student student = new Student();
            student.setFullName(request.getFullName() != null ? request.getFullName() : request.getName());
            student.setEmail(request.getEmail());
            student.setPassword(request.getPassword());
            student.setBirthDate(request.getBirthDate());
            student.setGender(request.getGender());
            student.setCollege(request.getCollege());
            student.setCourse(request.getCourse());
            student.setVerified(true);
            
            studentRepository.save(student);
            return "Registration successful. Check email for verification.";
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
    
    public org.springframework.http.ResponseEntity<?> login(LoginRequest request) {
        try {
            Optional<Student> student = studentRepository.findByEmail(request.getEmail());
            
            if (student.isEmpty()) {
                return org.springframework.http.ResponseEntity.badRequest().body("Invalid email or password");
            }
            
            if (!student.get().isVerified()) {
                return org.springframework.http.ResponseEntity.badRequest().body("Email not verified");
            }
            
            if (!request.getPassword().equals(student.get().getPassword())) {
                return org.springframework.http.ResponseEntity.badRequest().body("Invalid email or password");
            }
            
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("message", "Login successful");
            response.put("userId", student.get().getId());
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }
    
    public String verifyEmail(String token) {
        Optional<Student> student = studentRepository.findByVerificationToken(token);
        if (student.isEmpty()) return "Invalid token";
        
        student.get().setVerified(true);
        student.get().setVerificationToken(null);
        studentRepository.save(student.get());
        return "Email verified successfully";
    }
    

}