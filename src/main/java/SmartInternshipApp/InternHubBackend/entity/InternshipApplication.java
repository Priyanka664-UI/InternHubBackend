package SmartInternshipApp.InternHubBackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "internship_applications")
public class InternshipApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "internship_id", nullable = false)
    private Internship internship;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;
    
    @Column(name = "applied_date", nullable = false)
    private LocalDateTime appliedDate = LocalDateTime.now();
    
    @Column(name = "cover_letter", length = 2000)
    private String coverLetter;
    
    @Column(name = "college")
    private String college;
    
    @Column(name = "degree")
    private String degree;
    
    @Column(name = "year_of_study")
    private String yearOfStudy;
    
    @Column(name = "student_id_url")
    private String studentIdUrl;
    
    @Column(name = "resume_url")
    private String resumeUrl;
    
    public enum ApplicationStatus {
        PENDING, ACCEPTED, REJECTED, COMPLETED
    }
    
    // Constructors
    public InternshipApplication() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    public Internship getInternship() { return internship; }
    public void setInternship(Internship internship) { this.internship = internship; }
    
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
    
    public LocalDateTime getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDateTime appliedDate) { this.appliedDate = appliedDate; }
    
    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }
    
    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
    
    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }
    
    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }
    
    public String getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(String yearOfStudy) { this.yearOfStudy = yearOfStudy; }
    
    public String getStudentIdUrl() { return studentIdUrl; }
    public void setStudentIdUrl(String studentIdUrl) { this.studentIdUrl = studentIdUrl; }
}