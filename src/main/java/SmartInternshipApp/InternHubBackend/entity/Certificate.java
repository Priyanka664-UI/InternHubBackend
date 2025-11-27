package SmartInternshipApp.InternHubBackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificates")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "internship_id", nullable = false)
    private Internship internship;
    
    @Column(name = "certificate_number", unique = true, nullable = false)
    private String certificateNumber;
    
    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate = LocalDateTime.now();
    
    @Column(name = "completion_date", nullable = false)
    private LocalDateTime completionDate;
    
    @Column(name = "certificate_url")
    private String certificateUrl;
    
    @Column(name = "performance_rating")
    private Integer performanceRating; // 1-5 scale
    
    @Column(length = 1000)
    private String remarks;
    
    // Constructors
    public Certificate() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    public Internship getInternship() { return internship; }
    public void setInternship(Internship internship) { this.internship = internship; }
    
    public String getCertificateNumber() { return certificateNumber; }
    public void setCertificateNumber(String certificateNumber) { this.certificateNumber = certificateNumber; }
    
    public LocalDateTime getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDateTime issueDate) { this.issueDate = issueDate; }
    
    public LocalDateTime getCompletionDate() { return completionDate; }
    public void setCompletionDate(LocalDateTime completionDate) { this.completionDate = completionDate; }
    
    public String getCertificateUrl() { return certificateUrl; }
    public void setCertificateUrl(String certificateUrl) { this.certificateUrl = certificateUrl; }
    
    public Integer getPerformanceRating() { return performanceRating; }
    public void setPerformanceRating(Integer performanceRating) { this.performanceRating = performanceRating; }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}