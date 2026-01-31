package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Group;
import SmartInternshipApp.InternHubBackend.entity.GroupMember;
import SmartInternshipApp.InternHubBackend.entity.GroupInvitation;
import SmartInternshipApp.InternHubBackend.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class GroupController {
    
    @Autowired
    private GroupService groupService;
    
    @PostMapping("/create/{leaderId}")
    public ResponseEntity<Group> createGroup(@RequestBody Group group, @PathVariable Long leaderId) {
        try {
            Group createdGroup = groupService.createGroup(group, leaderId);
            return ResponseEntity.ok(createdGroup);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{groupId}/members/{studentId}")
    public ResponseEntity<GroupMember> addMember(@PathVariable Long groupId, 
                                               @PathVariable Long studentId,
                                               @RequestBody GroupMember memberData) {
        try {
            GroupMember member = groupService.addMemberToGroup(groupId, studentId, memberData);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/leader/{leaderId}")
    public ResponseEntity<List<Group>> getGroupsByLeader(@PathVariable Long leaderId) {
        try {
            System.out.println("Getting groups for leader ID: " + leaderId);
            List<Group> groups = groupService.getGroupsByLeader(leaderId);
            System.out.println("Found " + groups.size() + " groups");
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(List.of());
        }
    }
    
    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMember>> getGroupMembers(@PathVariable Long groupId) {
        List<GroupMember> members = groupService.getGroupMembers(groupId);
        return ResponseEntity.ok(members);
    }
    
    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        return groupService.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{groupId}/status")
    public ResponseEntity<Group> updateGroupStatus(@PathVariable Long groupId, 
                                                 @RequestParam Group.GroupStatus status) {
        try {
            Group updatedGroup = groupService.updateGroupStatus(groupId, status);
            return ResponseEntity.ok(updatedGroup);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{groupId}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Group> updateGroup(@PathVariable Long groupId, @RequestBody Group group) {
        try {
            System.out.println("Updating group with ID: " + groupId);
            Group updatedGroup = groupService.updateGroup(groupId, group);
            return ResponseEntity.ok(updatedGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{groupId}/invite")
    public ResponseEntity<String> inviteStudent(@PathVariable Long groupId, 
                                              @RequestParam String email,
                                              @RequestParam Long inviterId) {
        try {
            String token = groupService.inviteStudent(groupId, email, inviterId);
            return ResponseEntity.ok("Invitation sent. Token: " + token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/join/{token}")
    public ResponseEntity<GroupMember> acceptInvitation(@PathVariable String token,
                                                      @RequestBody GroupMember memberData) {
        try {
            GroupMember member = groupService.acceptInvitation(token, memberData);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/invitations/{email}")
    public ResponseEntity<List<GroupInvitation>> getInvitations(@PathVariable String email) {
        try {
            List<GroupInvitation> invitations = groupService.getInvitationsByEmail(email);
            return ResponseEntity.ok(invitations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(List.of());
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Group> getUserGroup(@PathVariable Long userId) {
        try {
            Group group = groupService.getUserGroup(userId);
            if (group != null) {
                return ResponseEntity.ok(group);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{groupId}/join-company/{companyId}")
    public ResponseEntity<Group> joinCompany(@PathVariable Long groupId, @PathVariable Long companyId) {
        try {
            Group updatedGroup = groupService.joinCompany(groupId, companyId);
            return ResponseEntity.ok(updatedGroup);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}