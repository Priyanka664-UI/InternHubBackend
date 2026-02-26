package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Group;
import SmartInternshipApp.InternHubBackend.entity.GroupMember;
import SmartInternshipApp.InternHubBackend.entity.GroupInvitation;
import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.entity.Company;
import SmartInternshipApp.InternHubBackend.repository.GroupRepository;
import SmartInternshipApp.InternHubBackend.repository.GroupMemberRepository;
import SmartInternshipApp.InternHubBackend.repository.GroupInvitationRepository;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
import SmartInternshipApp.InternHubBackend.repository.CompanyRepository;
import SmartInternshipApp.InternHubBackend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    
    @Autowired
    private GroupInvitationRepository invitationRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    public Group createGroup(Group group, Long leaderId) {
        Optional<Student> leader = studentRepository.findById(leaderId);
        if (leader.isPresent()) {
            group.setLeader(leader.get());
            Group savedGroup = groupRepository.save(group);
            
            // Automatically add leader as group member
            GroupMember leaderMember = new GroupMember();
            leaderMember.setGroup(savedGroup);
            leaderMember.setStudent(leader.get());
            leaderMember.setStudentName(leader.get().getFullName());
            leaderMember.setStatus(GroupMember.MemberStatus.APPROVED);
            groupMemberRepository.save(leaderMember);
            
            return savedGroup;
        }
        throw new RuntimeException("Leader not found");
    }
    
    public GroupMember addMemberToGroup(Long groupId, Long studentId, GroupMember memberData) {
        Optional<Group> group = groupRepository.findById(groupId);
        Optional<Student> student = studentRepository.findById(studentId);
        
        if (group.isPresent() && student.isPresent()) {
            if (groupMemberRepository.existsByGroupAndStudent(group.get(), student.get())) {
                throw new RuntimeException("Student already in group");
            }
            
            memberData.setGroup(group.get());
            memberData.setStudent(student.get());
            return groupMemberRepository.save(memberData);
        }
        throw new RuntimeException("Group or Student not found");
    }
    
    public String inviteStudent(Long groupId, String email, Long inviterId) {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            if (invitationRepository.existsByGroupIdAndInviteeEmail(groupId, email)) {
                throw new RuntimeException("Student already invited");
            }
            
            GroupInvitation invitation = new GroupInvitation();
            invitation.setGroup(group.get());
            invitation.setInviterId(inviterId);
            invitation.setInviteeEmail(email);
            invitation.setInvitationToken(java.util.UUID.randomUUID().toString());
            
            invitationRepository.save(invitation);
            return invitation.getInvitationToken();
        }
        throw new RuntimeException("Group not found");
    }
    
    public GroupMember acceptInvitation(String token, GroupMember memberData) {
        Optional<GroupInvitation> invitation = invitationRepository.findByInvitationToken(token);
        if (invitation.isPresent() && invitation.get().getStatus() == GroupInvitation.InvitationStatus.PENDING) {
            GroupInvitation inv = invitation.get();
            
            // Find student by email
            Optional<Student> student = studentRepository.findByEmail(inv.getInviteeEmail());
            if (student.isPresent()) {
                memberData.setGroup(inv.getGroup());
                memberData.setStudent(student.get());
                memberData.setStatus(GroupMember.MemberStatus.APPROVED);
                GroupMember member = groupMemberRepository.save(memberData);
                
                // Update invitation status
                inv.setStatus(GroupInvitation.InvitationStatus.ACCEPTED);
                inv.setInviteeId(student.get().getId());
                invitationRepository.save(inv);
                
                // Send notification to group leader
                Group group = inv.getGroup();
                if (group.getLeader() != null) {
                    notificationService.createNotification(
                        group.getLeader().getId(),
                        "Invitation Accepted",
                        student.get().getFullName() + " has accepted your invitation to join " + group.getGroupName(),
                        "INVITATION_ACCEPTED"
                    );
                }
                
                return member;
            }
        }
        throw new RuntimeException("Invalid or expired invitation");
    }
    
    public GroupMember acceptInvitationWithDocuments(String token, Long userId, String studentIdUrl, String resumeUrl, String requestLetterUrl, String githubLink) {
        Optional<GroupInvitation> invitation = invitationRepository.findByInvitationToken(token);
        if (invitation.isPresent() && invitation.get().getStatus() == GroupInvitation.InvitationStatus.PENDING) {
            GroupInvitation inv = invitation.get();
            
            Optional<Student> student = studentRepository.findById(userId);
            if (student.isPresent()) {
                // Check if already a member
                if (groupMemberRepository.existsByGroupAndStudent(inv.getGroup(), student.get())) {
                    throw new RuntimeException("Already a member of this group");
                }
                
                // Create group member with documents
                GroupMember member = new GroupMember();
                member.setGroup(inv.getGroup());
                member.setStudent(student.get());
                member.setStudentName(student.get().getFullName());
                member.setStudentIdDocumentUrl(studentIdUrl);
                member.setResumeUrl(resumeUrl);
                member.setInternshipRequestLetterUrl(requestLetterUrl);
                member.setGithubLink(githubLink);
                member.setStatus(GroupMember.MemberStatus.APPROVED);
                
                GroupMember savedMember = groupMemberRepository.save(member);
                
                // Update invitation status
                inv.setStatus(GroupInvitation.InvitationStatus.ACCEPTED);
                inv.setInviteeId(userId);
                invitationRepository.save(inv);
                
                return savedMember;
            }
        }
        throw new RuntimeException("Invalid or expired invitation");
    }
    
    public List<GroupInvitation> getInvitationsByEmail(String email) {
        try {
            return invitationRepository.findByInviteeEmailAndStatus(email, GroupInvitation.InvitationStatus.PENDING);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    public List<Group> getGroupsByLeader(Long leaderId) {
        try {
            System.out.println("Service: Getting groups for leader ID: " + leaderId);
            Optional<Student> leader = studentRepository.findById(leaderId);
            if (leader.isPresent()) {
                System.out.println("Leader found: " + leader.get().getFullName());
                List<Group> groups = groupRepository.findByLeader(leader.get());
                System.out.println("Found " + groups.size() + " groups in database");
                
                // Explicitly load company data for each group
                for (Group group : groups) {
                    if (group.getCompany() != null) {
                        // Force loading of company data
                        group.getCompany().getName();
                    }
                }
                
                return groups;
            } else {
                System.out.println("Leader not found for ID: " + leaderId);
            }
            return List.of();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    public List<GroupMember> getGroupMembers(Long groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            return groupMemberRepository.findByGroup(group.get());
        }
        return List.of();
    }
    
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }
    
    public Optional<Group> getGroupById(Long id) {
        Optional<Group> groupOpt = groupRepository.findById(id);
        if (groupOpt.isPresent()) {
            Group group = groupOpt.get();
            // Force load company data
            if (group.getCompany() != null) {
                group.getCompany().getName();
            }
        }
        return groupOpt;
    }
    
    public Group updateGroupStatus(Long groupId, Group.GroupStatus status) {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            group.get().setStatus(status);
            return groupRepository.save(group.get());
        }
        throw new RuntimeException("Group not found");
    }
    
    public Group updateGroup(Long groupId, Group updatedGroup) {
        Optional<Group> existingGroup = groupRepository.findById(groupId);
        if (existingGroup.isPresent()) {
            Group group = existingGroup.get();
            group.setGroupName(updatedGroup.getGroupName());
            group.setCollegeName(updatedGroup.getCollegeName());
            group.setDepartment(updatedGroup.getDepartment());
            group.setAcademicYear(updatedGroup.getAcademicYear());
            group.setSemester(updatedGroup.getSemester());
            group.setTotalStudents(updatedGroup.getTotalStudents());
            group.setInternshipType(updatedGroup.getInternshipType());
            group.setPreferredMode(updatedGroup.getPreferredMode());
            group.setDurationMonths(updatedGroup.getDurationMonths());
            group.setStartDate(updatedGroup.getStartDate());
            group.setEndDate(updatedGroup.getEndDate());
            group.setFacultyName(updatedGroup.getFacultyName());
            group.setFacultyEmail(updatedGroup.getFacultyEmail());
            group.setFacultyPhone(updatedGroup.getFacultyPhone());
            return groupRepository.save(group);
        }
        throw new RuntimeException("Group not found");
    }
    
    public Group getUserGroup(Long userId) {
        Optional<Student> student = studentRepository.findById(userId);
        if (student.isPresent()) {
            // Check if user is a leader of any group
            List<Group> leaderGroups = groupRepository.findByLeader(student.get());
            if (!leaderGroups.isEmpty()) {
                return leaderGroups.get(0);
            }
            
            // Check if user is a member of any group
            List<GroupMember> memberships = groupMemberRepository.findByStudent(student.get());
            if (!memberships.isEmpty() && memberships.get(0).getStatus() == GroupMember.MemberStatus.APPROVED) {
                return memberships.get(0).getGroup();
            }
        }
        return null;
    }
    
    public List<Group> getAllUserGroups(Long userId) {
        List<Group> userGroups = new java.util.ArrayList<>();
        Optional<Student> student = studentRepository.findById(userId);
        
        if (student.isPresent()) {
            // Add groups where user is leader
            List<Group> leaderGroups = groupRepository.findByLeader(student.get());
            for (Group group : leaderGroups) {
                // Force load company data
                if (group.getCompany() != null) {
                    group.getCompany().getName();
                }
                userGroups.add(group);
            }
            
            // Add groups where user is member
            List<GroupMember> memberships = groupMemberRepository.findByStudent(student.get());
            for (GroupMember membership : memberships) {
                if (membership.getStatus() == GroupMember.MemberStatus.APPROVED) {
                    Group group = membership.getGroup();
                    // Force load company data
                    if (group.getCompany() != null) {
                        group.getCompany().getName();
                    }
                    // Avoid duplicates if user is both leader and member
                    if (!userGroups.contains(group)) {
                        userGroups.add(group);
                    }
                }
            }
        }
        return userGroups;
    }
    
    public Group joinCompany(Long groupId, Long companyId) {
        System.out.println("joinCompany called: groupId=" + groupId + ", companyId=" + companyId);
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        
        if (groupOpt.isPresent() && companyOpt.isPresent()) {
            Group group = groupOpt.get();
            Company company = companyOpt.get();
            System.out.println("Setting company " + company.getName() + " for group " + group.getGroupName());
            group.setCompany(company);
            group.setStatus(Group.GroupStatus.APPLIED);
            Group savedGroup = groupRepository.save(group);
            System.out.println("Group saved with company: " + (savedGroup.getCompany() != null ? savedGroup.getCompany().getName() : "null"));
            return savedGroup;
        }
        throw new RuntimeException("Group or Company not found");
    }
    
    public Long getGroupCompanyId(Long groupId) {
        System.out.println("getGroupCompanyId called for groupId: " + groupId);
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            System.out.println("Group found: " + group.get().getGroupName());
            Company company = group.get().getCompany();
            System.out.println("Company: " + (company != null ? company.getName() + " (ID: " + company.getId() + ")" : "null"));
            if (company != null) {
                return company.getId();
            }
        } else {
            System.out.println("Group not found for ID: " + groupId);
        }
        return null;
    }

}