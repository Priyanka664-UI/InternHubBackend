package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.Attendance;
import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    Optional<Attendance> findByStudentAndAttendanceDate(Student student, LocalDate date);
    
    List<Attendance> findByGroupAndAttendanceDate(Group group, LocalDate date);
    
    List<Attendance> findByGroupOrderByAttendanceDateDescCheckInTimeDesc(Group group);
    
    @Query("SELECT a FROM Attendance a WHERE a.group.id = :groupId AND a.attendanceDate BETWEEN :startDate AND :endDate ORDER BY a.attendanceDate DESC, a.checkInTime DESC")
    List<Attendance> findByGroupAndDateRange(@Param("groupId") Long groupId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Attendance a WHERE a.student.id = :studentId ORDER BY a.attendanceDate DESC, a.checkInTime DESC")
    List<Attendance> findByStudentOrderByDateDesc(@Param("studentId") Long studentId);
}