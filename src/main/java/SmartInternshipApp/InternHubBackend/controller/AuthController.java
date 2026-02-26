package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.dto.RegistrationRequest;
import SmartInternshipApp.InternHubBackend.dto.LoginRequest;
import SmartInternshipApp.InternHubBackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        try {
            String result = authService.register(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("Received login request: " + request);
            
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }
            
            return authService.login(request);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Login failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody LoginRequest request) {
        try {
            return authService.adminLogin(request);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Login failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/company/login")
    public ResponseEntity<?> companyLogin(@RequestBody LoginRequest request) {
        try {
            return authService.companyLogin(request);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Login failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/company/setup-password")
    public ResponseEntity<?> setupCompanyPassword(@RequestBody LoginRequest request) {
        try {
            return authService.setupCompanyPassword(request);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Password setup failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/check-password")
    public ResponseEntity<?> checkPassword(@RequestBody LoginRequest request) {
        try {
            return authService.checkPassword(request);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Check failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/debug/company/{email}")
    public ResponseEntity<?> debugCompany(@PathVariable String email) {
        try {
            return authService.debugCompany(email);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Debug failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/create-test-user")
    public ResponseEntity<?> createTestUser() {
        try {
            RegistrationRequest testUser = new RegistrationRequest();
            testUser.setFullName("Test User");
            testUser.setEmail("test@test.com");
            testUser.setPassword("test123");
            testUser.setBirthDate(java.time.LocalDate.of(2000, 1, 1));
            testUser.setGender(SmartInternshipApp.InternHubBackend.entity.Student.Gender.MALE);
            testUser.setCollege("Test College");
            testUser.setCourse("Computer Science");
            
            String result = authService.register(testUser);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to create test user: " + e.getMessage());
        }
    }
    
    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            return authService.validateToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }
}