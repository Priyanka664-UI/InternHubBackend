package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface InternshipRepository extends JpaRepository<Internship, Long> {
    @Query("SELECT i FROM Internship i WHERE " +
           "LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.company) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.skillsRequired) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Internship> searchInternships(@Param("keyword") String keyword);
}