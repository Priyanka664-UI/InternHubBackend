package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.InternshipApplication;
import SmartInternshipApp.InternHubBackend.entity.InternshipApplication.ApplicationStatus;
import SmartInternshipApp.InternHubBackend.entity.InternshipApplication.ApplicationType;
import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Long> {
    List<InternshipApplication> findByStudentId(Long studentId);
    List<InternshipApplication> findByStudent(Student student);
    List<InternshipApplication> findByInternshipId(Long internshipId);
    List<InternshipApplication> findByStatus(ApplicationStatus status);
    List<InternshipApplication> findByApplicationType(ApplicationType applicationType);
    Optional<InternshipApplication> findByGroup(Group group);
    
    @Query("SELECT a FROM InternshipApplication a WHERE a.internship.companyId = ?1 AND a.applicationType = ?2")
    List<InternshipApplication> findByInternship_Company_IdAndApplicationType(Long companyId, ApplicationType applicationType);
    
    @Query("SELECT COUNT(a) FROM InternshipApplication a WHERE a.status = ?1")
    Long countByStatus(ApplicationStatus status);
    
    @Query("SELECT a FROM InternshipApplication a WHERE a.student.college = ?1")
    List<InternshipApplication> findByStudentCollege(String college);
}