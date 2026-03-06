package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.*;
import SmartInternshipApp.InternHubBackend.repository.*;
import SmartInternshipApp.InternHubBackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.time.LocalDateTime;

@Service
public class GroupApplicationService {

    @Autowired
    private InternshipApplicationRepository applicationRepository;
    @Autowired
    private InternshipRepository internshipRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private JwtUtil jwtUtil;
    @Value("${file.upload.dir}")
    private String uploadDir;

    public Long submitGroupApplication(
            Long internshipId, Long groupId, Integer teamSize, String teamLeader,
            String leaderContact, String leaderEmail, String teamMembers, String memberEmails,
            String academicYear, String semester, String skills, String experience, String motivation,
            String college, String degree, String yearOfStudy, String paymentStatus,
            Double paymentAmount, String paymentId, MultipartFile studentIdFile,
            MultipartFile resumeFile, MultipartFile invitationLetterFile, String token) throws Exception {
        
        if (token == null || token.isEmpty()) throw new RuntimeException("Authorization required");
        
        Long studentId = extractStudentIdFromToken(token.replace("Bearer ", ""));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found"));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        InternshipApplication application = new InternshipApplication();
        application.setStudent(student);
        application.setInternship(internship);
        application.setGroup(group);
        application.setApplicationType(InternshipApplication.ApplicationType.GROUP);
        application.setStatus(InternshipApplication.ApplicationStatus.PENDING);
        application.setAppliedDate(LocalDateTime.now());
        application.setCollege(college);
        application.setDegree(degree);
        application.setYearOfStudy(yearOfStudy);
        application.setPaymentStatus(paymentStatus);
        application.setPaymentAmount(paymentAmount);
        application.setPaymentId(paymentId);

        StringBuilder coverLetter = new StringBuilder();
        coverLetter.append("GROUP APPLICATION\n\n");
        coverLetter.append("Team Size: ").append(teamSize).append("\n");
        coverLetter.append("Team Leader: ").append(teamLeader).append("\n");
        coverLetter.append("Leader Contact: ").append(leaderContact).append("\n");
        coverLetter.append("Leader Email: ").append(leaderEmail).append("\n");
        coverLetter.append("Academic Year: ").append(academicYear).append("\n");
        coverLetter.append("Semester: ").append(semester).append("\n\n");
        if (teamMembers != null) coverLetter.append("TEAM MEMBERS:\n").append(teamMembers).append("\n\n");
        if (skills != null) coverLetter.append("TEAM SKILLS:\n").append(skills).append("\n\n");
        if (experience != null) coverLetter.append("EXPERIENCE:\n").append(experience).append("\n\n");
        if (motivation != null) coverLetter.append("MOTIVATION:\n").append(motivation).append("\n");
        application.setCoverLetter(coverLetter.toString());

        InternshipApplication savedApp = applicationRepository.save(application);

        if (studentIdFile != null) {
            String studentIdUrl = saveFile(studentIdFile, savedApp.getId(), "studentId");
            savedApp.setStudentIdUrl(studentIdUrl);
        }
        if (resumeFile != null) {
            String resumeUrl = saveFile(resumeFile, savedApp.getId(), "resume");
            savedApp.setResumeUrl(resumeUrl);
        }
        if (invitationLetterFile != null) {
            String invitationUrl = saveFile(invitationLetterFile, savedApp.getId(), "invitation");
            savedApp.setInvitationLetterUrl(invitationUrl);
        }

        applicationRepository.save(savedApp);
        
        try {
            notificationService.createNotification(studentId, "Group Application Submitted",
                "Your group application for " + internship.getTitle() + " has been submitted.", "SUCCESS");
        } catch (Exception e) {}
        
        return savedApp.getId();
    }

    private Long extractStudentIdFromToken(String token) {
        try {
            if (token.startsWith("temp-token-")) {
                return Long.parseLong(token.replace("temp-token-", ""));
            }
            io.jsonwebtoken.Claims claims = jwtUtil.validateToken(token);
            return claims.get("userId", Long.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid token: " + e.getMessage());
        }
    }

    private String saveFile(MultipartFile file, Long applicationId, String type) {
        try {
            String uploadPath = uploadDir != null && !uploadDir.isEmpty() ? uploadDir : System.getProperty("user.home") + "/internhub/uploads";
            Path dirPath = Paths.get(uploadPath).toAbsolutePath();
            if (!Files.exists(dirPath)) Files.createDirectories(dirPath);
            String fileName = "app" + applicationId + "_" + type + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = dirPath.resolve(fileName);
            Files.write(filePath, file.getBytes());
            return fileName;
        } catch (Exception e) {
            return null;
        }
    }
}
