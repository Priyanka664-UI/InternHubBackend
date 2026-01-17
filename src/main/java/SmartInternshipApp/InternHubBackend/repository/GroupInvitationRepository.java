package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.GroupInvitation;
import SmartInternshipApp.InternHubBackend.entity.GroupInvitation.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupInvitationRepository extends JpaRepository<GroupInvitation, Long> {
    
    List<GroupInvitation> findByInviteeEmailAndStatus(String inviteeEmail, InvitationStatus status);
    
    List<GroupInvitation> findByInviteeIdAndStatus(Long inviteeId, InvitationStatus status);
    
    List<GroupInvitation> findByGroupIdAndStatus(Long groupId, InvitationStatus status);
    
    List<GroupInvitation> findByGroupId(Long groupId);
    
    Optional<GroupInvitation> findByInvitationToken(String token);
    
    @Query("SELECT COUNT(gi) FROM GroupInvitation gi WHERE gi.inviteeEmail = :email AND gi.status = 'PENDING'")
    Long countPendingInvitationsByEmail(@Param("email") String email);
    
    @Query("SELECT COUNT(gi) FROM GroupInvitation gi WHERE gi.inviteeId = :userId AND gi.status = 'PENDING'")
    Long countPendingInvitationsByUserId(@Param("userId") Long userId);
    
    boolean existsByGroupIdAndInviteeEmail(Long groupId, String inviteeEmail);
}