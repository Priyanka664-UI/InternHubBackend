package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.dto.RegistrationRequest;
import SmartInternshipApp.InternHubBackend.dto.LoginRequest;
import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.entity.Company;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
import SmartInternshipApp.InternHubBackend.repository.CompanyRepository;
import SmartInternshipApp.InternHubBackend.util.JwtUtil;
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
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
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
            String token = jwtUtil.generateToken(student.get().getEmail(), "STUDENT", student.get().getId());
            response.put("token", token);
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
            Optional<Company> company = companyRepository.findByEmail(request.getEmail());
            
            if (company.isPresent()) {
                Company comp = company.get();
                
                if (comp.getUserType() == null || comp.getUserType() != 1) {
                    return org.springframework.http.ResponseEntity.status(403).body("You are not authorized as admin");
                }
                
                if (comp.getPassword() == null || comp.getPassword().isEmpty()) {
                    java.util.Map<String, Object> response = new java.util.HashMap<>();
                    response.put("requiresPasswordSetup", true);
                    response.put("email", comp.getEmail());
                    response.put("message", "Please set up your password");
                    return org.springframework.http.ResponseEntity.ok(response);
                }
                
                if (!request.getPassword().equals(comp.getPassword())) {
                    return org.springframework.http.ResponseEntity.badRequest().body("Invalid email or password");
                }
                
                java.util.Map<String, Object> response = new java.util.HashMap<>();
                String token = jwtUtil.generateToken(comp.getEmail(), "ADMIN", comp.getId());
                response.put("token", token);
                response.put("company", comp);
                response.put("userType", comp.getUserType());
                return org.springframework.http.ResponseEntity.ok(response);
            }
            
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
            String token = jwtUtil.generateToken(student.get().getEmail(), "ADMIN", student.get().getId());
            response.put("token", token);
            response.put("student", student.get());
            response.put("adminType", student.get().getIsAdmin());
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Admin login failed: " + e.getMessage());
        }
    }
    
    public org.springframework.http.ResponseEntity<?> companyLogin(LoginRequest request) {
        try {
            Optional<Company> company = companyRepository.findByEmail(request.getEmail());
            
            if (company.isEmpty()) {
                return org.springframework.http.ResponseEntity.badRequest().body("Invalid email or password");
            }
            
            Company comp = company.get();
            
            if (comp.getUserType() == null || comp.getUserType() != 2) {
                return org.springframework.http.ResponseEntity.status(403).body("You are not authorized as company");
            }
            
            if (comp.getPassword() == null || comp.getPassword().isEmpty()) {
                java.util.Map<String, Object> response = new java.util.HashMap<>();
                response.put("requiresPasswordSetup", true);
                response.put("email", comp.getEmail());
                response.put("message", "Please set up your password");
                return org.springframework.http.ResponseEntity.ok(response);
            }
            
            if (!request.getPassword().equals(comp.getPassword())) {
                return org.springframework.http.ResponseEntity.badRequest().body("Invalid email or password");
            }
            
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            String token = jwtUtil.generateToken(comp.getEmail(), "COMPANY", comp.getId());
            response.put("token", token);
            response.put("company", comp);
            response.put("userType", comp.getUserType());
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Company login failed: " + e.getMessage());
        }
    }
    
    public org.springframework.http.ResponseEntity<?> setupCompanyPassword(LoginRequest request) {
        try {
            System.out.println("Setup password request for email: " + request.getEmail());
            Optional<Company> company = companyRepository.findByEmail(request.getEmail());
            
            if (company.isEmpty()) {
                System.out.println("Company not found for email: " + request.getEmail());
                return org.springframework.http.ResponseEntity.badRequest().body("Company not found");
            }
            
            Company comp = company.get();
            System.out.println("Company found: " + comp.getName() + ", Current password: " + comp.getPassword());
            
            if (comp.getPassword() != null && !comp.getPassword().isEmpty()) {
                System.out.println("Password already set for company: " + comp.getName());
                return org.springframework.http.ResponseEntity.badRequest().body("Password already set. Please use login.");
            }
            
            System.out.println("Setting new password for company: " + comp.getName());
            comp.setPassword(request.getPassword());
            Company savedCompany = companyRepository.save(comp);
            System.out.println("Password saved. New password: " + savedCompany.getPassword());
            
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("success", true);
            response.put("message", "Password set successfully. You can now login.");
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error setting password: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Password setup failed: " + e.getMessage());
        }
    }
    
    public org.springframework.http.ResponseEntity<?> checkPassword(LoginRequest request) {
        try {
            // First check companies table
            Optional<Company> company = companyRepository.findByEmail(request.getEmail());
            
            if (company.isPresent()) {
                Company comp = company.get();
                java.util.Map<String, Object> response = new java.util.HashMap<>();
                
                if (comp.getPassword() == null || comp.getPassword().isEmpty()) {
                    response.put("requiresPasswordSetup", true);
                    response.put("email", comp.getEmail());
                    response.put("userType", comp.getUserType());
                    response.put("message", "Please set up your password");
                } else {
                    response.put("requiresPasswordSetup", false);
                    response.put("email", comp.getEmail());
                    response.put("userType", comp.getUserType());
                    response.put("message", "Password is set");
                }
                return org.springframework.http.ResponseEntity.ok(response);
            }
            
            // If not found in companies, check students table for admin users
            Optional<Student> student = studentRepository.findByEmail(request.getEmail());
            
            if (student.isEmpty()) {
                return org.springframework.http.ResponseEntity.badRequest().body("Email not found");
            }
            
            // Check if student is admin
            if (student.get().getIsAdmin() == null || (student.get().getIsAdmin() != 1 && student.get().getIsAdmin() != 2)) {
                return org.springframework.http.ResponseEntity.status(403).body("You are not authorized");
            }
            
            // Student admin found - password is always set for students
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("requiresPasswordSetup", false);
            response.put("email", student.get().getEmail());
            response.put("userType", 1); // Treat as main admin
            response.put("message", "Password is set");
            
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Check password failed: " + e.getMessage());
        }
    }
    
    public org.springframework.http.ResponseEntity<?> debugCompany(String email) {
        try {
            Optional<Company> company = companyRepository.findByEmail(email);
            
            if (company.isEmpty()) {
                return org.springframework.http.ResponseEntity.badRequest().body("Company not found");
            }
            
            Company comp = company.get();
            
            System.out.println("=== DEBUG COMPANY ===");
            System.out.println("ID: " + comp.getId());
            System.out.println("Name: " + comp.getName());
            System.out.println("Email: " + comp.getEmail());
            System.out.println("UserType: " + comp.getUserType());
            System.out.println("Password: " + comp.getPassword());
            System.out.println("Password is null: " + (comp.getPassword() == null));
            System.out.println("Password is empty: " + (comp.getPassword() != null && comp.getPassword().isEmpty()));
            System.out.println("===================");
            
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("id", comp.getId());
            response.put("name", comp.getName());
            response.put("email", comp.getEmail());
            response.put("userType", comp.getUserType());
            response.put("hasPassword", comp.getPassword() != null && !comp.getPassword().isEmpty());
            response.put("passwordLength", comp.getPassword() != null ? comp.getPassword().length() : 0);
            response.put("actualPassword", comp.getPassword()); // TEMPORARY - for debugging only
            
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Debug failed: " + e.getMessage());
        }
    }
    
    public org.springframework.http.ResponseEntity<?> validateToken(String token) {
        try {
            if (!jwtUtil.isTokenValid(token)) {
                return org.springframework.http.ResponseEntity.status(401).body("Invalid or expired token");
            }
            
            String email = jwtUtil.getEmailFromToken(token);
            io.jsonwebtoken.Claims claims = jwtUtil.validateToken(token);
            String userType = claims.get("userType", String.class);
            Long userId = claims.get("userId", Long.class);
            
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            
            if ("STUDENT".equals(userType)) {
                Optional<Student> student = studentRepository.findById(userId);
                if (student.isEmpty()) {
                    return org.springframework.http.ResponseEntity.status(401).body("User not found");
                }
                response.put("student", student.get());
                response.put("token", token);
            } else if ("COMPANY".equals(userType) || "ADMIN".equals(userType)) {
                Optional<Company> company = companyRepository.findById(userId);
                if (company.isPresent()) {
                    response.put("company", company.get());
                    response.put("userType", company.get().getUserType());
                    response.put("token", token);
                } else {
                    Optional<Student> student = studentRepository.findById(userId);
                    if (student.isEmpty()) {
                        return org.springframework.http.ResponseEntity.status(401).body("User not found");
                    }
                    response.put("student", student.get());
                    response.put("adminType", student.get().getIsAdmin());
                    response.put("token", token);
                }
            }
            
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.status(401).body("Token validation failed: " + e.getMessage());
        }
    }
}