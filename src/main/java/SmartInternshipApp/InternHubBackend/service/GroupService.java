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
                GroupMember member = groupMemberRepository.save(memberData);
                
                // Update invitation status
                inv.setStatus(GroupInvitation.InvitationStatus.ACCEPTED);
                invitationRepository.save(inv);
                
                return member;
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
        return groupRepository.findById(id);
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
            if (!memberships.isEmpty()) {
                return memberships.get(0).getGroup();
            }
        }
        return null;
    }
    
    public Group joinCompany(Long groupId, Long companyId) {
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        
        if (groupOpt.isPresent() && companyOpt.isPresent()) {
            Group group = groupOpt.get();
            group.setCompany(companyOpt.get());
            group.setStatus(Group.GroupStatus.APPLIED);
            return groupRepository.save(group);
        }
        throw new RuntimeException("Group or Company not found");
    }
    
    public Long getGroupCompanyId(Long groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent() && group.get().getCompany() != null) {
            return group.get().getCompany().getId();
        }
        return null;
    }

}