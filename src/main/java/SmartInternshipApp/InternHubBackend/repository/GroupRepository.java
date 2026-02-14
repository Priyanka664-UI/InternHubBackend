package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.Group;
import SmartInternshipApp.InternHubBackend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByLeader(Student leader);
    List<Group> findByStatus(Group.GroupStatus status);
    List<Group> findByCollegeName(String collegeName);
    List<Group> findByFacultyEmail(String facultyEmail);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Group g WHERE g.company.id = ?1")
    void deleteByCompanyId(Long companyId);
}