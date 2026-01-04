package SmartInternshipApp.InternHubBackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "group_members")
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
    @Column(name = "student_name")
    private String studentName;
    
    @Column(name = "github_link")
    private String githubLink;
    
    @Enumerated(EnumType.STRING)
    private MemberStatus status = MemberStatus.PENDING;
    
    // Constructors
    public GroupMember() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getGithubLink() { return githubLink; }
    public void setGithubLink(String githubLink) { this.githubLink = githubLink; }
    
    public MemberStatus getStatus() { return status; }
    public void setStatus(MemberStatus status) { this.status = status; }
    
    public enum MemberStatus {
        PENDING, APPROVED, REJECTED, SELECTED
    }
}