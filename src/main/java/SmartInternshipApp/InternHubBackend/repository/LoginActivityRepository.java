package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.LoginActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoginActivityRepository extends JpaRepository<LoginActivity, Long> {
    
    List<LoginActivity> findByStudentIdOrderByLoginTimeDesc(Long studentId);
    
    @Query("SELECT la FROM LoginActivity la WHERE la.studentId = :studentId AND la.loginTime >= :fromDate ORDER BY la.loginTime DESC")
    List<LoginActivity> findRecentLoginsByStudentId(@Param("studentId") Long studentId, @Param("fromDate") LocalDateTime fromDate);
    
    @Query("SELECT la FROM LoginActivity la WHERE la.studentId = :studentId AND la.isActive = true")
    List<LoginActivity> findActiveSessionsByStudentId(@Param("studentId") Long studentId);
}