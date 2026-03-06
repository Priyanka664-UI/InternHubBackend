package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.service.GroupApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/applications/group")
public class GroupApplicationController {

    @Autowired
    private GroupApplicationService applicationService;

    @PostMapping
    public ResponseEntity<?> submitGroupApplication(
            @RequestParam("internshipId") Long internshipId,
            @RequestParam("groupId") Long groupId,
            @RequestParam("teamSize") Integer teamSize,
            @RequestParam("teamLeader") String teamLeader,
            @RequestParam("leaderContact") String leaderContact,
            @RequestParam("leaderEmail") String leaderEmail,
            @RequestParam("teamMembers") String teamMembers,
            @RequestParam(value = "memberEmails", required = false) String memberEmails,
            @RequestParam("academicYear") String academicYear,
            @RequestParam("semester") String semester,
            @RequestParam(value = "skills", required = false) String skills,
            @RequestParam(value = "experience", required = false) String experience,
            @RequestParam("motivation") String motivation,
            @RequestParam("college") String college,
            @RequestParam("degree") String degree,
            @RequestParam("yearOfStudy") String yearOfStudy,
            @RequestParam(value = "paymentStatus", required = false) String paymentStatus,
            @RequestParam(value = "paymentAmount", required = false) Double paymentAmount,
            @RequestParam(value = "paymentId", required = false) String paymentId,
            @RequestParam("studentId") MultipartFile studentIdFile,
            @RequestParam("resume") MultipartFile resumeFile,
            @RequestParam("invitationLetter") MultipartFile invitationLetterFile,
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        try {
            Long applicationId = applicationService.submitGroupApplication(
                internshipId, groupId, teamSize, teamLeader, leaderContact, leaderEmail,
                teamMembers, memberEmails, academicYear, semester, skills, experience, motivation,
                college, degree, yearOfStudy, paymentStatus, paymentAmount, paymentId,
                studentIdFile, resumeFile, invitationLetterFile, token
            );
            return ResponseEntity.ok().body(java.util.Map.of("message", "Group application submitted successfully", "id", applicationId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", e.getMessage()));
        }
    }
}
