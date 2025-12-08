package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.Certificate;
import SmartInternshipApp.InternHubBackend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByStudentId(Long studentId);
    List<Certificate> findByStudent(Student student);
    List<Certificate> findByInternshipId(Long internshipId);
    Optional<Certificate> findByCertificateNumber(String certificateNumber);
    
    @Query("SELECT COUNT(c) FROM Certificate c")
    Long countTotalCertificates();
    
    @Query("SELECT AVG(c.performanceRating) FROM Certificate c WHERE c.performanceRating IS NOT NULL")
    Double getAveragePerformanceRating();
    
    @Query("SELECT c FROM Certificate c WHERE c.student.college = ?1")
    List<Certificate> findByStudentCollege(String college);
}