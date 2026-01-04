package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Attendance;
import SmartInternshipApp.InternHubBackend.entity.Attendance.AttendanceStatus;
import SmartInternshipApp.InternHubBackend.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    @PostMapping("/check-in")
    public ResponseEntity<Attendance> checkIn(@RequestBody Map<String, Long> request) {
        try {
            Long userId = request.get("userId");
            Long groupId = request.get("groupId");
            Attendance attendance = attendanceService.checkIn(userId, groupId);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/check-out")
    public ResponseEntity<Attendance> checkOut(@RequestBody Map<String, Long> request) {
        try {
            Long userId = request.get("userId");
            Attendance attendance = attendanceService.checkOut(userId);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/manual")
    public ResponseEntity<Attendance> markManualAttendance(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long groupId = Long.valueOf(request.get("groupId").toString());
            LocalDate date = LocalDate.parse(request.get("date").toString());
            AttendanceStatus status = AttendanceStatus.valueOf(request.get("status").toString());
            String notes = request.get("notes") != null ? request.get("notes").toString() : null;
            
            Attendance attendance = attendanceService.markManualAttendance(userId, groupId, date, status, notes);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Attendance>> getUserAttendance(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<Attendance> attendance = attendanceService.getUserAttendance(userId, startDate, endDate);
        return ResponseEntity.ok(attendance);
    }
    
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Attendance>> getGroupAttendance(
            @PathVariable Long groupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<Attendance> attendance = attendanceService.getGroupAttendance(groupId, date);
        return ResponseEntity.ok(attendance);
    }
    
    @GetMapping("/user/{userId}/hours")
    public ResponseEntity<Map<String, Double>> getUserTotalHours(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Double totalHours = attendanceService.getUserTotalHours(userId, startDate, endDate);
        return ResponseEntity.ok(Map.of("totalHours", totalHours));
    }
    
    @GetMapping("/user/{userId}/today")
    public ResponseEntity<Attendance> getTodayAttendance(@PathVariable Long userId) {
        return attendanceService.getTodayAttendance(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}