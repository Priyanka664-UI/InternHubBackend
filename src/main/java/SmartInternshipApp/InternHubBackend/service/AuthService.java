package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private JavaMailSender mailSender;
    
    public String register(Student student) {
        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            return "Email already exists";
        }
        
        student.setVerificationToken(UUID.randomUUID().toString());
        studentRepository.save(student);
        
        sendVerificationEmail(student.getEmail(), student.getVerificationToken());
        return "Registration successful. Check email for verification.";
    }
    
    public String login(String email, String password) {
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isEmpty()) return "User not found";
        
        if (!student.get().isVerified()) return "Email not verified";
        
        if (!password.equals(student.get().getPassword())) {
            return "Invalid credentials";
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
    
    private void sendVerificationEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email Verification");
        message.setText("Click to verify: http://localhost:9001/api/auth/verify?token=" + token);
        mailSender.send(message);
    }
}