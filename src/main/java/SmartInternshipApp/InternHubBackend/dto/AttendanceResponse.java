package SmartInternshipApp.InternHubBackend.dto;

import SmartInternshipApp.InternHubBackend.entity.Attendance;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AttendanceResponse {
    private Long id;
    private String studentName;
    private String groupName;
    private LocalDate attendanceDate;
    private LocalDateTime checkInTime;
    private Double distanceMeters;
    private String status;
    private String message;
    
    // Constructors
    public AttendanceResponse() {}
    
    public AttendanceResponse(Attendance attendance) {
        this.id = attendance.getId();
        this.studentName = attendance.getStudent().getFullName();
        this.groupName = attendance.getGroup().getGroupName();
        this.attendanceDate = attendance.getAttendanceDate();
        this.checkInTime = attendance.getCheckInTime();
        this.distanceMeters = attendance.getDistanceMeters();
        this.status = attendance.getStatus().toString();
    }
    
    public AttendanceResponse(String message) {
        this.message = message;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    
    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }
    
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }
    
    public Double getDistanceMeters() { return distanceMeters; }
    public void setDistanceMeters(Double distanceMeters) { this.distanceMeters = distanceMeters; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}