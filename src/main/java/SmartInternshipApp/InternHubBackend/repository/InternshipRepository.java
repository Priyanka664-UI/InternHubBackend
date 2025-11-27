package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {
    @Query("SELECT i FROM Internship i WHERE " +
           "LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.company) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.skillsRequired) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Internship> searchInternships(@Param("keyword") String keyword);
    
    List<Internship> findByStateId(Long stateId);
    List<Internship> findByCityId(Long cityId);
    List<Internship> findByWorkType(String workType);
    List<Internship> findByStateIdAndCityId(Long stateId, Long cityId);
}