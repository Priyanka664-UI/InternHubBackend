package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.dto.RegistrationRequest;
import SmartInternshipApp.InternHubBackend.dto.LoginRequest;
import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    public String register(RegistrationRequest request) {
        try {
            // Check if email already exists
            if (studentRepository.existsByEmail(request.getEmail())) {
                return "Email already exists";
            }
            
            // Check if username already exists (only if username is provided)
            if (request.getUsername() != null && studentRepository.existsByUsername(request.getUsername())) {
                return "Username already exists";
            }
            
            // Create new student
            Student student = new Student();
            student.setFullName(request.getFullName());
            student.setEmail(request.getEmail());
            student.setBirthDate(request.getBirthDate());
            student.setGender(request.getGender());
            student.setUsername(request.getUsername());
            student.setPassword(request.getPassword());
            student.setVerified(true);
            
            studentRepository.save(student);
            return "Registration successful";
        } catch (Exception e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }
    
    public String login(LoginRequest request) {
        Optional<Student> student = studentRepository.findByUsername(request.getUsername());
        if (student.isEmpty()) {
            return "Invalid username or password";
        }
        
        if (!student.get().isVerified()) {
            return "Account not verified";
        }
        
        if (!request.getPassword().equals(student.get().getPassword())) {
            return "Invalid username or password";
        }
        
        return "Login successful";
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