package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Attendance;
import SmartInternshipApp.InternHubBackend.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/attendance")
@CrossOrigin(origins = "*")
public class AdminAttendanceController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    @PostMapping("/mark")
    public ResponseEntity<Attendance> markAttendance(@RequestBody Map<String, Object> request) {
        Long studentId = Long.valueOf(request.get("studentId").toString());
        Long internshipId = Long.valueOf(request.get("internshipId").toString());
        LocalDate date = LocalDate.parse(request.get("date").toString());
        LocalTime checkIn = request.get("checkInTime") != null ? 
            LocalTime.parse(request.get("checkInTime").toString()) : null;
        LocalTime checkOut = request.get("checkOutTime") != null ? 
            LocalTime.parse(request.get("checkOutTime").toString()) : null;
        Attendance.AttendanceStatus status = Attendance.AttendanceStatus.valueOf(request.get("status").toString());
        
        Attendance attendance = attendanceService.markAttendance(studentId, internshipId, date, checkIn, checkOut, status);
        return ResponseEntity.ok(attendance);
    }
    
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByStudent(studentId));
    }
    
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Attendance>> getAttendanceByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(attendanceService.getAttendanceByDate(localDate));
    }
    
    @GetMapping("/stats/{studentId}")
    public ResponseEntity<Map<String, Object>> getAttendanceStats(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getAttendanceStats(studentId));
    }
}