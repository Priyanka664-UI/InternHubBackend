package SmartInternshipApp.InternHubBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "groups")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "group_name")
    private String groupName;
    
    @Column(name = "college_name")
    private String collegeName;
    
    private String department;
    
    @Column(name = "academic_year")
    private String academicYear;
    
    private String semester;
    
    @Column(name = "total_students")
    private Integer totalStudents;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "internship_type")
    private InternshipType internshipType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_mode")
    private InternshipMode preferredMode;
    
    @Column(name = "duration_months")
    private Integer durationMonths;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "faculty_name")
    private String facultyName;
    
    @Column(name = "faculty_email")
    private String facultyEmail;
    
    @Column(name = "faculty_phone")
    private String facultyPhone;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Student leader;
    
    @Enumerated(EnumType.STRING)
    private GroupStatus status = GroupStatus.PENDING;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Company company;
    
    // Constructors
    public Group() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    
    public String getCollegeName() { return collegeName; }
    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public Integer getTotalStudents() { return totalStudents; }
    public void setTotalStudents(Integer totalStudents) { this.totalStudents = totalStudents; }
    
    public InternshipType getInternshipType() { return internshipType; }
    public void setInternshipType(InternshipType internshipType) { this.internshipType = internshipType; }
    
    public InternshipMode getPreferredMode() { return preferredMode; }
    public void setPreferredMode(InternshipMode preferredMode) { this.preferredMode = preferredMode; }
    
    public Integer getDurationMonths() { return durationMonths; }
    public void setDurationMonths(Integer durationMonths) { this.durationMonths = durationMonths; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
    
    public String getFacultyEmail() { return facultyEmail; }
    public void setFacultyEmail(String facultyEmail) { this.facultyEmail = facultyEmail; }
    
    public String getFacultyPhone() { return facultyPhone; }
    public void setFacultyPhone(String facultyPhone) { this.facultyPhone = facultyPhone; }
    
    public Student getLeader() { return leader; }
    public void setLeader(Student leader) { this.leader = leader; }
    
    public GroupStatus getStatus() { return status; }
    public void setStatus(GroupStatus status) { this.status = status; }
    
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
    
    public enum InternshipType {
        TECHNICAL, NON_TECHNICAL
    }
    
    public enum InternshipMode {
        ONLINE, OFFLINE, HYBRID
    }
    
    public enum GroupStatus {
        PENDING, APPROVED, REJECTED, APPLIED, SELECTED
    }
}