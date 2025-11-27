package SmartInternshipApp.InternHubBackend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "internship_id", nullable = false)
    private Internship internship;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(name = "check_in_time")
    private LocalTime checkInTime;
    
    @Column(name = "check_out_time")
    private LocalTime checkOutTime;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status = AttendanceStatus.ABSENT;
    
    @Column(length = 500)
    private String remarks;
    
    @Column(name = "hours_worked")
    private Double hoursWorked;
    
    public enum AttendanceStatus {
        PRESENT, ABSENT, LATE, HALF_DAY, LEAVE
    }
    
    // Constructors
    public Attendance() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    public Internship getInternship() { return internship; }
    public void setInternship(Internship internship) { this.internship = internship; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public LocalTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalTime checkInTime) { this.checkInTime = checkInTime; }
    
    public LocalTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalTime checkOutTime) { this.checkOutTime = checkOutTime; }
    
    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    public Double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(Double hoursWorked) { this.hoursWorked = hoursWorked; }
}