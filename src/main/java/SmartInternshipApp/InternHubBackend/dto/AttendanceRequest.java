package SmartInternshipApp.InternHubBackend.dto;

public class AttendanceRequest {
    private Long studentId;
    private Long groupId;
    private Double latitude;
    private Double longitude;
    
    // Constructors
    public AttendanceRequest() {}
    
    public AttendanceRequest(Long studentId, Long groupId, Double latitude, Double longitude) {
        this.studentId = studentId;
        this.groupId = groupId;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // Getters and Setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    
    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}