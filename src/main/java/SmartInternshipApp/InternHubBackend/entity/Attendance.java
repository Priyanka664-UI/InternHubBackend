package SmartInternshipApp.InternHubBackend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
    
    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;
    
    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;
    
    @Column(name = "student_latitude", nullable = false)
    private Double studentLatitude;
    
    @Column(name = "student_longitude", nullable = false)
    private Double studentLongitude;
    
    @Column(name = "company_latitude")
    private Double companyLatitude;
    
    @Column(name = "company_longitude")
    private Double companyLongitude;
    
    @Column(name = "distance_meters")
    private Double distanceMeters;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttendanceStatus status;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public Attendance() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }
    
    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }
    
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }
    
    public Double getStudentLatitude() { return studentLatitude; }
    public void setStudentLatitude(Double studentLatitude) { this.studentLatitude = studentLatitude; }
    
    public Double getStudentLongitude() { return studentLongitude; }
    public void setStudentLongitude(Double studentLongitude) { this.studentLongitude = studentLongitude; }
    
    public Double getCompanyLatitude() { return companyLatitude; }
    public void setCompanyLatitude(Double companyLatitude) { this.companyLatitude = companyLatitude; }
    
    public Double getCompanyLongitude() { return companyLongitude; }
    public void setCompanyLongitude(Double companyLongitude) { this.companyLongitude = companyLongitude; }
    
    public Double getDistanceMeters() { return distanceMeters; }
    public void setDistanceMeters(Double distanceMeters) { this.distanceMeters = distanceMeters; }
    
    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public enum AttendanceStatus {
        PRESENT, ABSENT, LOCATION_MISMATCH
    }
}