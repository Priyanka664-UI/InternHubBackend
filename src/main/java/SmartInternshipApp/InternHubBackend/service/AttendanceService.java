package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Attendance;
import SmartInternshipApp.InternHubBackend.entity.Attendance.AttendanceStatus;
import SmartInternshipApp.InternHubBackend.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    public Attendance checkIn(Long userId, Long groupId) {
        LocalDate today = LocalDate.now();
        Optional<Attendance> existing = attendanceRepository.findByUserIdAndDate(userId, today);
        
        if (existing.isPresent()) {
            throw new RuntimeException("Already checked in today");
        }
        
        Attendance attendance = new Attendance(userId, groupId, today);
        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setStatus(AttendanceStatus.PRESENT);
        
        return attendanceRepository.save(attendance);
    }
    
    public Attendance checkOut(Long userId) {
        LocalDate today = LocalDate.now();
        Optional<Attendance> existing = attendanceRepository.findByUserIdAndDate(userId, today);
        
        if (!existing.isPresent()) {
            throw new RuntimeException("No check-in found for today");
        }
        
        Attendance attendance = existing.get();
        if (attendance.getCheckOutTime() != null) {
            throw new RuntimeException("Already checked out today");
        }
        
        attendance.setCheckOutTime(LocalDateTime.now());
        
        // Calculate total hours
        if (attendance.getCheckInTime() != null) {
            Duration duration = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime());
            double hours = duration.toMinutes() / 60.0;
            attendance.setTotalHours(Math.round(hours * 100.0) / 100.0);
        }
        
        return attendanceRepository.save(attendance);
    }
    
    public Attendance markManualAttendance(Long userId, Long groupId, LocalDate date, AttendanceStatus status, String notes) {
        Optional<Attendance> existing = attendanceRepository.findByUserIdAndDate(userId, date);
        
        Attendance attendance;
        if (existing.isPresent()) {
            attendance = existing.get();
        } else {
            attendance = new Attendance(userId, groupId, date);
        }
        
        attendance.setStatus(status);
        attendance.setNotes(notes);
        attendance.setIsManual(true);
        
        if (status == AttendanceStatus.PRESENT && attendance.getTotalHours() == null) {
            attendance.setTotalHours(8.0); // Default 8 hours for manual present
        }
        
        return attendanceRepository.save(attendance);
    }
    
    public List<Attendance> getUserAttendance(Long userId, LocalDate startDate, LocalDate endDate) {
        try {
            return attendanceRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<Attendance> getGroupAttendance(Long groupId, LocalDate date) {
        return attendanceRepository.findGroupAttendanceByDate(groupId, date);
    }
    
    public Double getUserTotalHours(Long userId, LocalDate startDate, LocalDate endDate) {
        Double total = attendanceRepository.getTotalHoursByUserAndDateRange(userId, startDate, endDate);
        return total != null ? total : 0.0;
    }
    
    public Optional<Attendance> getTodayAttendance(Long userId) {
        try {
            return attendanceRepository.findByUserIdAndDate(userId, LocalDate.now());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}