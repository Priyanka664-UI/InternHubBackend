package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.Attendance;
import SmartInternshipApp.InternHubBackend.entity.Attendance.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    Optional<Attendance> findByUserIdAndDate(Long userId, LocalDate date);
    
    List<Attendance> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    
    List<Attendance> findByGroupIdAndDate(Long groupId, LocalDate date);
    
    List<Attendance> findByGroupIdAndDateBetween(Long groupId, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT SUM(a.totalHours) FROM Attendance a WHERE a.userId = :userId AND a.date BETWEEN :startDate AND :endDate")
    Double getTotalHoursByUserAndDateRange(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.userId = :userId AND a.status = :status AND a.date BETWEEN :startDate AND :endDate")
    Long countByUserIdAndStatusAndDateRange(@Param("userId") Long userId, @Param("status") AttendanceStatus status, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Attendance a WHERE a.groupId = :groupId AND a.date = :date ORDER BY a.userId")
    List<Attendance> findGroupAttendanceByDate(@Param("groupId") Long groupId, @Param("date") LocalDate date);
}