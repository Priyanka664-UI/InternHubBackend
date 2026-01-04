package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.LoginActivity;
import SmartInternshipApp.InternHubBackend.entity.TwoFactorAuth;
import SmartInternshipApp.InternHubBackend.repository.LoginActivityRepository;
import SmartInternshipApp.InternHubBackend.repository.TwoFactorAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/security")
@CrossOrigin(origins = "*")
public class SecurityController {

    @Autowired
    private TwoFactorAuthRepository twoFactorAuthRepository;

    @Autowired
    private LoginActivityRepository loginActivityRepository;

    // Get 2FA status
    @GetMapping("/2fa/status/{studentId}")
    public ResponseEntity<?> get2FAStatus(@PathVariable Long studentId) {
        try {
            Optional<TwoFactorAuth> twoFactorAuth = twoFactorAuthRepository.findByStudentId(studentId);
            if (twoFactorAuth.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "enabled", twoFactorAuth.get().getIsEnabled(),
                    "hasSecret", true
                ));
            }
            return ResponseEntity.ok(Map.of(
                "enabled", false,
                "hasSecret", false
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting 2FA status: " + e.getMessage());
        }
    }

    // Setup 2FA
    @PostMapping("/2fa/setup/{studentId}")
    public ResponseEntity<?> setup2FA(@PathVariable Long studentId) {
        try {
            Optional<TwoFactorAuth> existing = twoFactorAuthRepository.findByStudentId(studentId);
            
            String secretKey = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            String backupCodes = generateBackupCodes();
            
            TwoFactorAuth twoFactorAuth;
            if (existing.isPresent()) {
                twoFactorAuth = existing.get();
                twoFactorAuth.setSecretKey(secretKey);
                twoFactorAuth.setBackupCodes(backupCodes);
            } else {
                twoFactorAuth = new TwoFactorAuth(studentId, secretKey);
                twoFactorAuth.setBackupCodes(backupCodes);
            }
            
            twoFactorAuthRepository.save(twoFactorAuth);
            
            return ResponseEntity.ok(Map.of(
                "secretKey", secretKey,
                "backupCodes", backupCodes.split(","),
                "qrCodeUrl", "https://chart.googleapis.com/chart?chs=200x200&chld=M|0&cht=qr&chl=otpauth://totp/InternHub:" + studentId + "?secret=" + secretKey + "&issuer=InternHub"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error setting up 2FA: " + e.getMessage());
        }
    }

    // Enable/Disable 2FA
    @PutMapping("/2fa/toggle/{studentId}")
    public ResponseEntity<?> toggle2FA(@PathVariable Long studentId, @RequestBody Map<String, Boolean> request) {
        try {
            Optional<TwoFactorAuth> twoFactorAuth = twoFactorAuthRepository.findByStudentId(studentId);
            if (twoFactorAuth.isPresent()) {
                twoFactorAuth.get().setIsEnabled(request.get("enabled"));
                twoFactorAuthRepository.save(twoFactorAuth.get());
                return ResponseEntity.ok(Map.of("success", true, "enabled", request.get("enabled")));
            }
            return ResponseEntity.badRequest().body("2FA not set up for this user");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error toggling 2FA: " + e.getMessage());
        }
    }

    // Get login activity
    @GetMapping("/login-activity/{studentId}")
    public ResponseEntity<?> getLoginActivity(@PathVariable Long studentId) {
        try {
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
            List<LoginActivity> activities = loginActivityRepository.findRecentLoginsByStudentId(studentId, thirtyDaysAgo);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting login activity: " + e.getMessage());
        }
    }

    // Log login activity
    @PostMapping("/login-activity")
    public ResponseEntity<?> logLoginActivity(@RequestBody Map<String, Object> request) {
        try {
            Long studentId = Long.valueOf(request.get("studentId").toString());
            String ipAddress = (String) request.get("ipAddress");
            String deviceInfo = (String) request.get("deviceInfo");
            String location = (String) request.get("location");
            
            LoginActivity activity = new LoginActivity(studentId, ipAddress, deviceInfo, location);
            loginActivityRepository.save(activity);
            
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error logging login activity: " + e.getMessage());
        }
    }

    // Terminate session
    @PutMapping("/login-activity/terminate/{activityId}")
    public ResponseEntity<?> terminateSession(@PathVariable Long activityId) {
        try {
            Optional<LoginActivity> activity = loginActivityRepository.findById(activityId);
            if (activity.isPresent()) {
                activity.get().setIsActive(false);
                activity.get().setLogoutTime(LocalDateTime.now());
                loginActivityRepository.save(activity.get());
                return ResponseEntity.ok(Map.of("success", true));
            }
            return ResponseEntity.badRequest().body("Session not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error terminating session: " + e.getMessage());
        }
    }

    private String generateBackupCodes() {
        StringBuilder codes = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (i > 0) codes.append(",");
            codes.append(String.format("%08d", (int) (Math.random() * 100000000)));
        }
        return codes.toString();
    }
}