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
            student.setFullName(request.getFullName());
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
            System.out.println("Login attempt for email: " + request.getEmail());
            Optional<Student> student = studentRepository.findByEmail(request.getEmail());
            
            if (student.isEmpty()) {
                System.out.println("No student found with email: " + request.getEmail());
                return org.springframework.http.ResponseEntity.badRequest().body("Invalid email or password");
            }
            
            System.out.println("Student found: " + student.get().getEmail() + ", verified: " + student.get().isVerified());
            
            if (!student.get().isVerified()) {
                return org.springframework.http.ResponseEntity.badRequest().body("Email not verified");
            }
            
            System.out.println("Password check - Input: '" + request.getPassword() + "', Stored: '" + student.get().getPassword() + "'");
            
            if (!request.getPassword().equals(student.get().getPassword())) {
                return org.springframework.http.ResponseEntity.badRequest().body("Invalid email or password");
            }
            
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("token", "temp-token-" + student.get().getId());
            response.put("student", student.get());
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
    
    public org.springframework.http.ResponseEntity<?> adminLogin(LoginRequest request) {
        try {
            Optional<Student> student = studentRepository.findByEmail(request.getEmail());
            
            if (student.isEmpty()) {
                return org.springframework.http.ResponseEntity.badRequest().body("Invalid email or password");
            }
            
            if (!request.getPassword().equals(student.get().getPassword())) {
                return org.springframework.http.ResponseEntity.badRequest().body("Invalid email or password");
            }
            
            if (student.get().getIsAdmin() == null || (student.get().getIsAdmin() != 1 && student.get().getIsAdmin() != 2)) {
                return org.springframework.http.ResponseEntity.status(403).body("You are not admin");
            }
            
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("token", "admin-token-" + student.get().getId());
            response.put("student", student.get());
            response.put("adminType", student.get().getIsAdmin());
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Admin login failed: " + e.getMessage());
        }
    }
    
    public org.springframework.http.ResponseEntity<?> companyLogin(LoginRequest request) {
        try {
            Optional<Student> student = studentRepository.findByEmail(request.getEmail());
            
            if (student.isEmpty()) {
                return org.springframework.http.ResponseEntity.badRequest().body("Invalid email or password");
            }
            
            if (!request.getPassword().equals(student.get().getPassword())) {
                return org.springframework.http.ResponseEntity.badRequest().body("Invalid email or password");
            }
            
            if (student.get().getIsAdmin() == null || student.get().getIsAdmin() != 2) {
                return org.springframework.http.ResponseEntity.status(403).body("You are not authorized as company admin");
            }
            
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("token", "company-token-" + student.get().getId());
            response.put("student", student.get());
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Company login failed: " + e.getMessage());
        }
    }
}