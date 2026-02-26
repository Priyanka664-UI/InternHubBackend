package SmartInternshipApp.InternHubBackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "group_member_documents")
public class GroupMemberDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private InternshipApplication application;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @Column(name = "student_id_url")
    private String studentIdUrl;
    
    @Column(name = "resume_url")
    private String resumeUrl;
    
    @Column(name = "uploaded_date")
    private LocalDateTime uploadedDate;
    
    @Column(name = "status")
    private String status = "PENDING"; // PENDING, UPLOADED
    
    // Constructors
    public GroupMemberDocument() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public InternshipApplication getApplication() { return application; }
    public void setApplication(InternshipApplication application) { this.application = application; }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    public String getStudentIdUrl() { return studentIdUrl; }
    public void setStudentIdUrl(String studentIdUrl) { this.studentIdUrl = studentIdUrl; }
    
    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
    
    public LocalDateTime getUploadedDate() { return uploadedDate; }
    public void setUploadedDate(LocalDateTime uploadedDate) { this.uploadedDate = uploadedDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
