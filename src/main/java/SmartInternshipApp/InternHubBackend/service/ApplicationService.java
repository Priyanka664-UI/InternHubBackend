package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Internship;
import SmartInternshipApp.InternHubBackend.entity.InternshipApplication;
import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.repository.InternshipApplicationRepository;
import SmartInternshipApp.InternHubBackend.repository.InternshipRepository;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
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

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Autowired
    private NotificationService notificationService;

    public void submitApplication(Long internshipId, String college, String degree, 
                                   String yearOfStudy, MultipartFile studentIdFile, 
                                   MultipartFile resumeFile, String token) throws Exception {
        
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

        String coverLetter = String.format("College: %s\nDegree: %s\nYear: %s", 
                college, degree, yearOfStudy);
        application.setCoverLetter(coverLetter);

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
            notificationService.createNotification(
                studentId,
                "Application Submitted",
                "Your application for " + internship.getTitle() + " has been submitted successfully.",
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
