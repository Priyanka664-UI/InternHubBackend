package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.HelpSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HelpSupportRepository extends JpaRepository<HelpSupport, Long> {
    List<HelpSupport> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    List<HelpSupport> findAllByOrderByCreatedAtDesc();
}
