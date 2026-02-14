package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.CompanyReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyReviewRepository extends JpaRepository<CompanyReview, Long> {
    List<CompanyReview> findByCompanyId(Long companyId);
    
    @Query("SELECT AVG(cr.rating) FROM CompanyReview cr WHERE cr.company.id = ?1")
    Double getAverageRating(Long companyId);
    
    @Query("SELECT COUNT(cr) FROM CompanyReview cr WHERE cr.company.id = ?1")
    Long getReviewCount(Long companyId);
}
