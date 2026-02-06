package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.Company;
import SmartInternshipApp.InternHubBackend.entity.CompanyCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT c FROM Company c WHERE " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.industry) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Company> findByKeyword(@Param("keyword") String keyword);
    
    List<Company> findByCategory(CompanyCategory category);
    
    @Query("SELECT c FROM Company c WHERE c.category.id = :categoryId")
    List<Company> findByCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT c FROM Company c WHERE c.category = :category AND (" +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.industry) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Company> findByCategoryAndKeyword(@Param("category") CompanyCategory category, @Param("keyword") String keyword);
    
    Optional<Company> findByEmail(String email);
}