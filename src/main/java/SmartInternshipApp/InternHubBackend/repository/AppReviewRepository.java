package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.AppReview;
import SmartInternshipApp.InternHubBackend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppReviewRepository extends JpaRepository<AppReview, Long> {
    
    @Query("SELECT ar FROM AppReview ar ORDER BY ar.createdAt DESC")
    List<AppReview> findAllOrderByCreatedAtDesc();
    
    Optional<AppReview> findByStudent(Student student);
    
    boolean existsByStudent(Student student);
}