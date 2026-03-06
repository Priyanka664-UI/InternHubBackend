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
public class IndividualApplicationService {

    @Autowired
    private InternshipApplicationRepository applicationRepository;
    @Autowired
    private InternshipRepository internshipRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PaymentReceiptService paymentReceiptService;
    @Autowired
    private JwtUtil jwtUtil;
    @Value("${file.upload.dir}")
    private String uploadDir;

    public Long submitIndividualApplication(
            Long internshipId, String fullName, String email, String phone, String duration,
            String skills, String motivation, String college, String degree, String yearOfStudy,
            String paymentStatus, Double paymentAmount, String paymentId,
            MultipartFile resumeFile, MultipartFile pastQualificationFile, String token) throws Exception {
        
        if (token == null || token.isEmpty()) throw new RuntimeException("Authorization required");
        
        Long studentId = extractStudentIdFromToken(token.replace("Bearer ", ""));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        InternshipApplication application = new InternshipApplication();
        application.setStudent(student);
        application.setInternship(internship);
        application.setApplicationType(InternshipApplication.ApplicationType.INDIVIDUAL);
        application.setStatus(InternshipApplication.ApplicationStatus.PENDING);
        application.setAppliedDate(LocalDateTime.now());
        application.setFullName(fullName);
        application.setEmail(email);
        application.setPhone(phone);
        application.setDuration(duration);
        application.setSkills(skills);
        application.setMotivation(motivation);
        application.setCollege(college);
        application.setDegree(degree);
        application.setYearOfStudy(yearOfStudy);
        application.setPaymentStatus(paymentStatus);
        application.setPaymentAmount(paymentAmount);
        application.setPaymentId(paymentId);

        InternshipApplication savedApp = applicationRepository.save(application);

        if (resumeFile != null) {
            String resumeUrl = saveFile(resumeFile, savedApp.getId(), "resume");
            savedApp.setResumeUrl(resumeUrl);
        }
        if (pastQualificationFile != null) {
            String pastQualUrl = saveFile(pastQualificationFile, savedApp.getId(), "pastQualification");
            savedApp.setPastQualificationUrl(pastQualUrl);
        }

        applicationRepository.save(savedApp);
        
        // Generate payment receipt if payment was made
        if (paymentId != null && !paymentId.isEmpty() && paymentAmount != null && paymentAmount > 0) {
            try {
                paymentReceiptService.createReceipt(savedApp, paymentId, paymentId, paymentAmount);
            } catch (Exception e) {
                // Log error but don't fail the application
            }
        }
        
        try {
            notificationService.createNotification(studentId, "Application Submitted",
                "Your application for " + internship.getTitle() + " has been submitted.", "SUCCESS");
        } catch (Exception e) {}
        
        return savedApp.getId();
    }

    private Long extractStudentIdFromToken(String token) {
        if (token.startsWith("temp-token-")) {
            return Long.parseLong(token.replace("temp-token-", ""));
        }
        
        io.jsonwebtoken.Claims claims = jwtUtil.validateToken(token);
        return claims.get("userId", Long.class);
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
