package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.Attendance;
import SmartInternshipApp.InternHubBackend.entity.Attendance.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByInternshipId(Long internshipId);
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByStudentIdAndDateBetween(Long studentId, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = ?1 AND a.status = ?2")
    Long countByStudentIdAndStatus(Long studentId, AttendanceStatus status);
    
    @Query("SELECT AVG(a.hoursWorked) FROM Attendance a WHERE a.student.id = ?1 AND a.hoursWorked IS NOT NULL")
    Double getAverageHoursWorkedByStudent(Long studentId);
    
    @Query("SELECT a FROM Attendance a WHERE a.date BETWEEN ?1 AND ?2")
    List<Attendance> findByDateRange(LocalDate startDate, LocalDate endDate);
}