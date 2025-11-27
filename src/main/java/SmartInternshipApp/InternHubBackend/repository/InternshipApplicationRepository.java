package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.InternshipApplication;
import SmartInternshipApp.InternHubBackend.entity.InternshipApplication.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Long> {
    List<InternshipApplication> findByStudentId(Long studentId);
    List<InternshipApplication> findByInternshipId(Long internshipId);
    List<InternshipApplication> findByStatus(ApplicationStatus status);
    
    @Query("SELECT COUNT(a) FROM InternshipApplication a WHERE a.status = ?1")
    Long countByStatus(ApplicationStatus status);
    
    @Query("SELECT a FROM InternshipApplication a WHERE a.student.college = ?1")
    List<InternshipApplication> findByStudentCollege(String college);
}