package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.dto.AttendanceRequest;
import SmartInternshipApp.InternHubBackend.dto.AttendanceResponse;
import SmartInternshipApp.InternHubBackend.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    @PostMapping("/checkin")
    public ResponseEntity<AttendanceResponse> markAttendance(@RequestBody AttendanceRequest request) {
        AttendanceResponse response = attendanceService.markAttendance(request);
        
        if (response.getMessage() != null && response.getMessage().contains("successfully")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<AttendanceResponse>> getGroupAttendance(@PathVariable Long groupId) {
        List<AttendanceResponse> attendances = attendanceService.getAttendanceByGroup(groupId);
        return ResponseEntity.ok(attendances);
    }
    
    @GetMapping("/group/{groupId}/date-range")
    public ResponseEntity<List<AttendanceResponse>> getGroupAttendanceByDateRange(
            @PathVariable Long groupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<AttendanceResponse> attendances = attendanceService.getAttendanceByGroupAndDate(groupId, startDate, endDate);
        return ResponseEntity.ok(attendances);
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceResponse>> getStudentAttendance(@PathVariable Long studentId) {
        List<AttendanceResponse> attendances = attendanceService.getStudentAttendance(studentId);
        return ResponseEntity.ok(attendances);
    }
}