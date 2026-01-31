package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Internship;
import SmartInternshipApp.InternHubBackend.entity.InternshipApplication;
import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.entity.Group;
import SmartInternshipApp.InternHubBackend.repository.InternshipApplicationRepository;
import SmartInternshipApp.InternHubBackend.repository.InternshipRepository;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
import SmartInternshipApp.InternHubBackend.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class ApplicationService {

    @Autowired
    private InternshipApplicationRepository applicationRepository;

    @Autowired
    private InternshipRepository internshipRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Autowired
    private NotificationService notificationService;

    public void submitApplication(Long internshipId, String college, String degree, 
                                   String yearOfStudy, Long groupId, String applicationType,
                                   Integer teamSize, String teamLeader, String leaderContact, String leaderEmail,
                                   String teamMembers, String memberEmails, String academicYear, String semester,
                                   String skills, String experience, String motivation,
                                   MultipartFile studentIdFile, MultipartFile resumeFile, String token) throws Exception {
        
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Authorization token is required");
        }
        
        Long studentId = extractStudentIdFromToken(token.replace("Bearer ", ""));
        
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found with ID: " + internshipId));

        InternshipApplication application = new InternshipApplication();
        application.setStudent(student);
        application.setInternship(internship);
        application.setAppliedDate(LocalDateTime.now());
        application.setStatus(InternshipApplication.ApplicationStatus.PENDING);
        
        // Handle group application
        if (groupId != null && "GROUP".equals(applicationType)) {
            Group group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));
            application.setGroup(group);
            application.setApplicationType(InternshipApplication.ApplicationType.GROUP);
            
            // Build comprehensive cover letter for group application
            StringBuilder coverLetter = new StringBuilder();
            coverLetter.append("GROUP APPLICATION\n\n");
            coverLetter.append("College: ").append(college).append("\n");
            coverLetter.append("Degree: ").append(degree).append("\n");
            coverLetter.append("Year of Study: ").append(yearOfStudy).append("\n\n");
            
            coverLetter.append("TEAM DETAILS:\n");
            coverLetter.append("Team Size: ").append(teamSize).append("\n");
            coverLetter.append("Team Leader: ").append(teamLeader).append("\n");
            coverLetter.append("Leader Contact: ").append(leaderContact).append("\n");
            coverLetter.append("Leader Email: ").append(leaderEmail).append("\n");
            if (academicYear != null) coverLetter.append("Academic Year: ").append(academicYear).append("\n");
            if (semester != null) coverLetter.append("Semester: ").append(semester).append("\n\n");
            
            if (teamMembers != null && !teamMembers.trim().isEmpty()) {
                coverLetter.append("TEAM MEMBERS:\n").append(teamMembers).append("\n\n");
            }
            if (memberEmails != null && !memberEmails.trim().isEmpty()) {
                coverLetter.append("MEMBER EMAILS:\n").append(memberEmails).append("\n\n");
            }
            if (skills != null && !skills.trim().isEmpty()) {
                coverLetter.append("TEAM SKILLS:\n").append(skills).append("\n\n");
            }
            if (experience != null && !experience.trim().isEmpty()) {
                coverLetter.append("PREVIOUS EXPERIENCE:\n").append(experience).append("\n\n");
            }
            if (motivation != null && !motivation.trim().isEmpty()) {
                coverLetter.append("MOTIVATION:\n").append(motivation).append("\n");
            }
            
            application.setCoverLetter(coverLetter.toString());
        } else {
            application.setApplicationType(InternshipApplication.ApplicationType.INDIVIDUAL);
            String coverLetter = String.format("College: %s\nDegree: %s\nYear: %s", 
                    college, degree, yearOfStudy);
            application.setCoverLetter(coverLetter);
        }

        application.setCollege(college);
        application.setDegree(degree);
        application.setYearOfStudy(yearOfStudy);

        InternshipApplication savedApp = applicationRepository.save(application);

        if (studentIdFile != null && !studentIdFile.isEmpty()) {
            String studentIdUrl = saveFile(studentIdFile, savedApp.getId(), "studentId");
            savedApp.setStudentIdUrl(studentIdUrl);
        }

        if (resumeFile != null && !resumeFile.isEmpty()) {
            String resumeUrl = saveFile(resumeFile, savedApp.getId(), "resume");
            savedApp.setResumeUrl(resumeUrl);
        }
        
        applicationRepository.save(savedApp);
         
        try {
            String notificationMessage = groupId != null ? 
                "Your group application for " + internship.getTitle() + " has been submitted successfully." :
                "Your application for " + internship.getTitle() + " has been submitted successfully.";
                
            notificationService.createNotification(
                studentId,
                "Application Submitted",
                notificationMessage,
                "SUCCESS"
            );
        } catch (Exception e) {
            System.err.println("Failed to create notification: " + e.getMessage());
        }
    }

    private Long extractStudentIdFromToken(String token) {
        if (token.startsWith("temp-token-")) {
            return Long.parseLong(token.replace("temp-token-", ""));
        }
        throw new RuntimeException("Invalid token format");
    }

    private String saveFile(MultipartFile file, Long applicationId, String type) throws Exception {
        Path dirPath = Paths.get(uploadDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        String fileName = "app" + applicationId + "_" + type + "_" + System.currentTimeMillis() + 
                "_" + file.getOriginalFilename();
        Path filePath = dirPath.resolve(fileName);
        Files.write(filePath, file.getBytes());
        
        return fileName;
    }
}
