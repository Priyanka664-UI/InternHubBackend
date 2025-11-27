package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.*;
import SmartInternshipApp.InternHubBackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class AttendanceService {
    
    @Autowired private AttendanceRepository attendanceRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private InternshipRepository internshipRepository;
    
    public Attendance markAttendance(Long studentId, Long internshipId, LocalDate date, 
                                   LocalTime checkIn, LocalTime checkOut, Attendance.AttendanceStatus status) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        Internship internship = internshipRepository.findById(internshipId)
            .orElseThrow(() -> new RuntimeException("Internship not found"));
        
        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setInternship(internship);
        attendance.setDate(date);
        attendance.setCheckInTime(checkIn);
        attendance.setCheckOutTime(checkOut);
        attendance.setStatus(status);
        
        if (checkIn != null && checkOut != null) {
            Duration duration = Duration.between(checkIn, checkOut);
            attendance.setHoursWorked(duration.toMinutes() / 60.0);
        }
        
        return attendanceRepository.save(attendance);
    }
    
    public List<Attendance> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
    
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }
    
    public Map<String, Object> getAttendanceStats(Long studentId) {
        Map<String, Object> stats = new HashMap<>();
        
        Long presentDays = attendanceRepository.countByStudentIdAndStatus(studentId, Attendance.AttendanceStatus.PRESENT);
        Long absentDays = attendanceRepository.countByStudentIdAndStatus(studentId, Attendance.AttendanceStatus.ABSENT);
        Long lateDays = attendanceRepository.countByStudentIdAndStatus(studentId, Attendance.AttendanceStatus.LATE);
        Double avgHours = attendanceRepository.getAverageHoursWorkedByStudent(studentId);
        
        stats.put("presentDays", presentDays);
        stats.put("absentDays", absentDays);
        stats.put("lateDays", lateDays);
        stats.put("averageHoursWorked", avgHours);
        
        Long totalDays = presentDays + absentDays + lateDays;
        if (totalDays > 0) {
            stats.put("attendancePercentage", (presentDays * 100.0) / totalDays);
        }
        
        return stats;
    }
    
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }
}