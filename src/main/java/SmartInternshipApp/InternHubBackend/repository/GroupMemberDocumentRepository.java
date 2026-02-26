package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.GroupMemberDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberDocumentRepository extends JpaRepository<GroupMemberDocument, Long> {
    List<GroupMemberDocument> findByApplicationId(Long applicationId);
    Optional<GroupMemberDocument> findByApplicationIdAndStudentId(Long applicationId, Long studentId);
}
