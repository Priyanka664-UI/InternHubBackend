package SmartInternshipApp.InternHubBackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "two_factor_auth")
public class TwoFactorAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_id", nullable = false, unique = true)
    private Long studentId;
    
    @Column(name = "secret_key", nullable = false)
    private String secretKey;
    
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = false;
    
    @Column(name = "backup_codes")
    private String backupCodes;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public TwoFactorAuth() {
        this.createdAt = LocalDateTime.now();
    }

    public TwoFactorAuth(Long studentId, String secretKey) {
        this.studentId = studentId;
        this.secretKey = secretKey;
        this.createdAt = LocalDateTime.now();
        this.isEnabled = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }

    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { 
        this.isEnabled = isEnabled;
        this.updatedAt = LocalDateTime.now();
    }

    public String getBackupCodes() { return backupCodes; }
    public void setBackupCodes(String backupCodes) { this.backupCodes = backupCodes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}