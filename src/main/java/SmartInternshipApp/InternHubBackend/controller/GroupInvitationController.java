package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Group;
import SmartInternshipApp.InternHubBackend.entity.GroupInvitation;
import SmartInternshipApp.InternHubBackend.entity.GroupInvitation.InvitationStatus;
import SmartInternshipApp.InternHubBackend.repository.GroupRepository;
import SmartInternshipApp.InternHubBackend.repository.GroupInvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/group-invitations")
@CrossOrigin(origins = "*")
public class GroupInvitationController {
    
    @Autowired
    private GroupInvitationRepository invitationRepository;
    
    @Autowired
    private GroupRepository groupRepository;
    
    @PostMapping("/send")
    public ResponseEntity<?> sendInvitation(@RequestBody InvitationRequest request) {
        try {
            // Check if group exists
            Optional<Group> groupOpt = groupRepository.findById(request.getGroupId());
            if (!groupOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Group not found");
            }
            
            Group group = groupOpt.get();
            
            // Check if invitation already exists
            if (invitationRepository.existsByGroupIdAndInviteeEmail(request.getGroupId(), request.getInviteeEmail())) {
                return ResponseEntity.badRequest().body("Invitation already sent to this email");
            }
            
            // Create invitation
            String token = UUID.randomUUID().toString();
            GroupInvitation invitation = new GroupInvitation(
                group, 
                request.getInviterId(), 
                request.getInviteeEmail(), 
                request.getMessage(),
                token
            );
            
            GroupInvitation saved = invitationRepository.save(invitation);
            
            // TODO: Send email notification
            
            return ResponseEntity.ok(saved);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending invitation: " + e.getMessage());
        }
    }
    
    @GetMapping("/pending/{email}")
    public ResponseEntity<List<GroupInvitation>> getPendingInvitations(@PathVariable String email) {
        try {
            List<GroupInvitation> invitations = invitationRepository.findByInviteeEmailAndStatus(email, InvitationStatus.PENDING);
            return ResponseEntity.ok(invitations);
        } catch (Exception e) {
            // Return empty list if table doesn't exist or other database error
            return ResponseEntity.ok(List.of());
        }
    }
    
    @GetMapping("/count/{email}")
    public ResponseEntity<Long> getInvitationCount(@PathVariable String email) {
        try {
            Long count = invitationRepository.countPendingInvitationsByEmail(email);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            // Return 0 if table doesn't exist or other database error
            return ResponseEntity.ok(0L);
        }
    }
    
    @PostMapping("/respond/{token}")
    public ResponseEntity<?> respondToInvitation(@PathVariable String token, @RequestBody InvitationResponse response) {
        try {
            Optional<GroupInvitation> invitationOpt = invitationRepository.findByInvitationToken(token);
            if (!invitationOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Invalid invitation token");
            }
            
            GroupInvitation invitation = invitationOpt.get();
            
            if (invitation.getStatus() != InvitationStatus.PENDING) {
                return ResponseEntity.badRequest().body("Invitation already responded to");
            }
            
            invitation.setStatus(response.isAccepted() ? InvitationStatus.ACCEPTED : InvitationStatus.REJECTED);
            invitation.setRespondedAt(LocalDateTime.now());
            invitation.setInviteeId(response.getUserId());
            
            invitationRepository.save(invitation);
            
            return ResponseEntity.ok("Invitation " + (response.isAccepted() ? "accepted" : "rejected"));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error responding to invitation: " + e.getMessage());
        }
    }
    
    // DTOs
    public static class InvitationRequest {
        private Long groupId;
        private Long inviterId;
        private String inviteeEmail;
        private String message;
        
        // Getters and Setters
        public Long getGroupId() { return groupId; }
        public void setGroupId(Long groupId) { this.groupId = groupId; }
        
        public Long getInviterId() { return inviterId; }
        public void setInviterId(Long inviterId) { this.inviterId = inviterId; }
        
        public String getInviteeEmail() { return inviteeEmail; }
        public void setInviteeEmail(String inviteeEmail) { this.inviteeEmail = inviteeEmail; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class InvitationResponse {
        private boolean accepted;
        private Long userId;
        
        // Getters and Setters
        public boolean isAccepted() { return accepted; }
        public void setAccepted(boolean accepted) { this.accepted = accepted; }
        
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }
}