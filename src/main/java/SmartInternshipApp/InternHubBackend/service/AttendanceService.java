package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.dto.AttendanceRequest;
import SmartInternshipApp.InternHubBackend.dto.AttendanceResponse;
import SmartInternshipApp.InternHubBackend.entity.*;
import SmartInternshipApp.InternHubBackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    
    private static final double ALLOWED_RADIUS_METERS = 100.0;
    
    public AttendanceResponse markAttendance(AttendanceRequest request) {
        try {
            // Validate student
            Optional<Student> studentOpt = studentRepository.findById(request.getStudentId());
            if (!studentOpt.isPresent()) {
                return new AttendanceResponse("Student not found");
            }
            Student student = studentOpt.get();
            
            // Validate group
            Optional<Group> groupOpt = groupRepository.findById(request.getGroupId());
            if (!groupOpt.isPresent()) {
                return new AttendanceResponse("Group not found");
            }
            Group group = groupOpt.get();
            
            // Verify student is member of the group or is the group leader
            if (!groupMemberRepository.existsByGroupAndStudent(group, student)) {
                // If student is the group leader, they can check in
                if (group.getLeader() == null || !group.getLeader().getId().equals(student.getId())) {
                    return new AttendanceResponse("You are not enrolled in this internship group");
                }
            }
            
            // Check if attendance already marked today
            LocalDate today = LocalDate.now();
            Optional<Attendance> existingAttendance = attendanceRepository.findByStudentAndAttendanceDate(student, today);
            if (existingAttendance.isPresent()) {
                Attendance existing = existingAttendance.get();
                return new AttendanceResponse("You have already checked in today at " + 
                    existing.getCheckInTime().toLocalTime().toString());
            }
            
            // Get company location
            Company company = group.getCompany();
            if (company == null || company.getLatitude() == null || company.getLongitude() == null) {
                return new AttendanceResponse("Company location not configured");
            }
            
            // Calculate distance
            double distance = calculateDistance(
                request.getLatitude(), request.getLongitude(),
                company.getLatitude(), company.getLongitude()
            );
            
            // Create attendance record
            Attendance attendance = new Attendance();
            attendance.setStudent(student);
            attendance.setGroup(group);
            attendance.setAttendanceDate(today);
            attendance.setCheckInTime(LocalDateTime.now());
            attendance.setStudentLatitude(request.getLatitude());
            attendance.setStudentLongitude(request.getLongitude());
            attendance.setCompanyLatitude(company.getLatitude());
            attendance.setCompanyLongitude(company.getLongitude());
            attendance.setDistanceMeters(distance);
            
            // Determine status based on distance
            if (distance <= ALLOWED_RADIUS_METERS) {
                attendance.setStatus(Attendance.AttendanceStatus.PRESENT);
            } else {
                attendance.setStatus(Attendance.AttendanceStatus.LOCATION_MISMATCH);
                attendanceRepository.save(attendance);
                return new AttendanceResponse("You are not at the internship location. Distance: " + Math.round(distance) + " meters");
            }
            
            attendanceRepository.save(attendance);
            
            AttendanceResponse response = new AttendanceResponse(attendance);
            response.setMessage("Attendance marked successfully");
            return response;
            
        } catch (Exception e) {
            return new AttendanceResponse("Error marking attendance: " + e.getMessage());
        }
    }
    
    public List<AttendanceResponse> getAttendanceByGroup(Long groupId) {
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if (!groupOpt.isPresent()) {
            return List.of();
        }
        
        List<Attendance> attendances = attendanceRepository.findByGroupOrderByAttendanceDateDescCheckInTimeDesc(groupOpt.get());
        return attendances.stream()
                .map(AttendanceResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<AttendanceResponse> getAttendanceByGroupAndDate(Long groupId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendances = attendanceRepository.findByGroupAndDateRange(groupId, startDate, endDate);
        return attendances.stream()
                .map(AttendanceResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<AttendanceResponse> getStudentAttendance(Long studentId) {
        List<Attendance> attendances = attendanceRepository.findByStudentOrderByDateDesc(studentId);
        return attendances.stream()
                .map(AttendanceResponse::new)
                .collect(Collectors.toList());
    }
    
    // Haversine formula to calculate distance between two points
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Earth's radius in meters
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
}