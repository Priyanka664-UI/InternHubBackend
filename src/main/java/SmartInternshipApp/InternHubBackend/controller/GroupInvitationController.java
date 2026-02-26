package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Group;
import SmartInternshipApp.InternHubBackend.entity.GroupInvitation;
import SmartInternshipApp.InternHubBackend.entity.GroupInvitation.InvitationStatus;
import SmartInternshipApp.InternHubBackend.entity.GroupMember;
import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.repository.GroupRepository;
import SmartInternshipApp.InternHubBackend.repository.GroupInvitationRepository;
import SmartInternshipApp.InternHubBackend.repository.GroupMemberRepository;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
import SmartInternshipApp.InternHubBackend.service.NotificationService;
import SmartInternshipApp.InternHubBackend.service.GroupService;
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
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    
    @PostMapping("/send")
    public ResponseEntity<?> sendInvitation(@RequestBody InvitationRequest request) {
        try {
            System.out.println("Received invitation request: " + request.getGroupId() + ", " + request.getInviterId() + ", " + request.getInviteeEmail());
            
            // Validate request
            if (request.getGroupId() == null) {
                return ResponseEntity.badRequest().body("Group ID is required");
            }
            if (request.getInviterId() == null) {
                return ResponseEntity.badRequest().body("Inviter ID is required");
            }
            if (request.getInviteeEmail() == null || request.getInviteeEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Invitee email is required");
            }
            
            // Check if group exists
            Optional<Group> groupOpt = groupRepository.findById(request.getGroupId());
            if (!groupOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Group not found");
            }
            
            Group group = groupOpt.get();
            
            // Check if invitation already exists
            if (invitationRepository.existsByGroupIdAndInviteeEmail(request.getGroupId(), request.getInviteeEmail())) {
                return ResponseEntity.badRequest().body("Already added to team");
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
            System.out.println("Invitation saved successfully with ID: " + saved.getId());
            
            // Create notification for the invitee
            Optional<Student> inviteeStudent = studentRepository.findByEmail(request.getInviteeEmail());
            if (inviteeStudent.isPresent()) {
                String title = "Group Invitation";
                String message = "You have been invited to join the group: " + group.getGroupName();
                notificationService.createNotification(inviteeStudent.get().getId(), title, message, "GROUP_INVITATION");
            }
            
            // TODO: Send email notification
            
            return ResponseEntity.ok(saved);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error sending invitation: " + e.getMessage());
        }
    }
    
    @GetMapping("/pending/{email}")
    public ResponseEntity<List<GroupInvitation>> getPendingInvitations(@PathVariable String email) {
        try {
            System.out.println("Fetching invitations for email: " + email);
            List<GroupInvitation> invitations = invitationRepository.findByInviteeEmailAndStatus(email, InvitationStatus.PENDING);
            System.out.println("Found " + invitations.size() + " invitations");
            return ResponseEntity.ok(invitations);
        } catch (Exception e) {
            System.err.println("Error fetching invitations: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(List.of());
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<GroupInvitation>> getAllInvitations() {
        try {
            List<GroupInvitation> invitations = invitationRepository.findAll();
            System.out.println("Total invitations in database: " + invitations.size());
            return ResponseEntity.ok(invitations);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }
    
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<GroupInvitation>> getGroupInvitations(@PathVariable Long groupId) {
        try {
            List<GroupInvitation> invitations = invitationRepository.findByGroupId(groupId);
            return ResponseEntity.ok(invitations);
        } catch (Exception e) {
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
            
            // If accepted, add member to group
            if (response.isAccepted()) {
                Optional<Student> inviteeStudent = studentRepository.findById(response.getUserId());
                if (inviteeStudent.isPresent()) {
                    // Check if not already a member
                    if (!groupMemberRepository.existsByGroupAndStudent(invitation.getGroup(), inviteeStudent.get())) {
                        GroupMember member = new GroupMember();
                        member.setGroup(invitation.getGroup());
                        member.setStudent(inviteeStudent.get());
                        member.setStudentName(inviteeStudent.get().getFullName());
                        member.setStatus(GroupMember.MemberStatus.APPROVED);
                        groupMemberRepository.save(member);
                    }
                }
            }
            
            // Send notification to group creator/leader when invitation is accepted or rejected
            Optional<Student> inviteeStudent = studentRepository.findById(response.getUserId());
            
            if (inviteeStudent.isPresent()) {
                // Get group leader (creator)
                Group group = invitation.getGroup();
                if (group.getLeader() != null) {
                    String title = response.isAccepted() ? "Invitation Accepted" : "Invitation Rejected";
                    String message = inviteeStudent.get().getFullName() + 
                            (response.isAccepted() ? 
                                " has accepted your invitation to join " : 
                                " has rejected your invitation to join ") + 
                            group.getGroupName();
                    
                    notificationService.createNotification(
                        group.getLeader().getId(), 
                        title, 
                        message, 
                        response.isAccepted() ? "INVITATION_ACCEPTED" : "INVITATION_REJECTED"
                    );
                }
            }
            
            return ResponseEntity.ok("Invitation " + (response.isAccepted() ? "accepted" : "rejected"));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error responding to invitation: " + e.getMessage());
        }
    }
    
    @PostMapping("/accept-with-documents/{token}")
    public ResponseEntity<?> acceptInvitationWithDocuments(@PathVariable String token, @RequestBody AcceptInvitationRequest request) {
        try {
            // Validate required documents for group applications
            if (request.getStudentIdDocumentUrl() == null || request.getStudentIdDocumentUrl().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Student ID document is required");
            }
            if (request.getResumeUrl() == null || request.getResumeUrl().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Resume is required");
            }
            if (request.getInternshipRequestLetterUrl() == null || request.getInternshipRequestLetterUrl().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Internship request letter is required");
            }
            
            // Call service to accept invitation and create group member with documents
            groupService.acceptInvitationWithDocuments(
                token,
                request.getUserId(),
                request.getStudentIdDocumentUrl(),
                request.getResumeUrl(),
                request.getInternshipRequestLetterUrl(),
                request.getGithubLink()
            );
            
            // Get invitation for notification
            Optional<GroupInvitation> invitationOpt = invitationRepository.findByInvitationToken(token);
            if (invitationOpt.isPresent()) {
                GroupInvitation invitation = invitationOpt.get();
                
                // Send notification to group creator/leader
                Optional<Student> inviteeStudent = studentRepository.findById(request.getUserId());
                
                if (inviteeStudent.isPresent()) {
                    Group group = invitation.getGroup();
                    if (group.getLeader() != null) {
                        String title = "Invitation Accepted";
                        String message = inviteeStudent.get().getFullName() + 
                                " has accepted your invitation and uploaded documents for " + 
                                group.getGroupName();
                        notificationService.createNotification(
                            group.getLeader().getId(), 
                            title, 
                            message, 
                            "INVITATION_ACCEPTED"
                        );
                    }
                }
                
                return ResponseEntity.ok().body(new AcceptInvitationResponse(
                    "Invitation accepted successfully",
                    invitation.getId(),
                    invitation.getGroup().getId()
                ));
            }
            
            return ResponseEntity.ok("Invitation accepted successfully");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error accepting invitation: " + e.getMessage());
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
    
    public static class AcceptInvitationRequest {
        private Long userId;
        private String studentIdDocumentUrl;
        private String resumeUrl;
        private String internshipRequestLetterUrl;
        private String githubLink;
        
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        
        public String getStudentIdDocumentUrl() { return studentIdDocumentUrl; }
        public void setStudentIdDocumentUrl(String studentIdDocumentUrl) { this.studentIdDocumentUrl = studentIdDocumentUrl; }
        
        public String getResumeUrl() { return resumeUrl; }
        public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
        
        public String getInternshipRequestLetterUrl() { return internshipRequestLetterUrl; }
        public void setInternshipRequestLetterUrl(String internshipRequestLetterUrl) { this.internshipRequestLetterUrl = internshipRequestLetterUrl; }
        
        public String getGithubLink() { return githubLink; }
        public void setGithubLink(String githubLink) { this.githubLink = githubLink; }
    }
    
    public static class AcceptInvitationResponse {
        private String message;
        private Long invitationId;
        private Long groupId;
        
        public AcceptInvitationResponse(String message, Long invitationId, Long groupId) {
            this.message = message;
            this.invitationId = invitationId;
            this.groupId = groupId;
        }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public Long getInvitationId() { return invitationId; }
        public void setInvitationId(Long invitationId) { this.invitationId = invitationId; }
        
        public Long getGroupId() { return groupId; }
        public void setGroupId(Long groupId) { this.groupId = groupId; }
    }
}