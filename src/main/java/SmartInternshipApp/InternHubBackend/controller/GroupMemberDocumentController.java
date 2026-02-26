package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.GroupMemberDocument;
import SmartInternshipApp.InternHubBackend.entity.InternshipApplication;
import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.repository.GroupMemberDocumentRepository;
import SmartInternshipApp.InternHubBackend.repository.InternshipApplicationRepository;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/group-member-documents")
public class GroupMemberDocumentController {

    @Autowired
    private GroupMemberDocumentRepository documentRepository;

    @Autowired
    private InternshipApplicationRepository applicationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Value("${file.upload.dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMemberDocuments(
            @RequestParam("applicationId") Long applicationId,
            @RequestParam("studentId") MultipartFile studentIdFile,
            @RequestParam("resume") MultipartFile resumeFile,
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        try {
            Long studentId = extractStudentIdFromToken(token.replace("Bearer ", ""));
            
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            InternshipApplication application = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            // Check if document already exists
            GroupMemberDocument document = documentRepository
                    .findByApplicationIdAndStudentId(applicationId, studentId)
                    .orElse(new GroupMemberDocument());

            document.setApplication(application);
            document.setStudent(student);
            document.setUploadedDate(LocalDateTime.now());
            document.setStatus("UPLOADED");

            // Save files
            if (studentIdFile != null && !studentIdFile.isEmpty()) {
                String studentIdUrl = saveFile(studentIdFile, studentId, "studentId");
                document.setStudentIdUrl(studentIdUrl);
            }

            if (resumeFile != null && !resumeFile.isEmpty()) {
                String resumeUrl = saveFile(resumeFile, studentId, "resume");
                document.setResumeUrl(resumeUrl);
            }

            documentRepository.save(document);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Documents uploaded successfully");
            response.put("documentId", document.getId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<?> getMemberDocuments(@PathVariable Long applicationId) {
        try {
            List<GroupMemberDocument> documents = documentRepository.findByApplicationId(applicationId);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/my-status/{applicationId}")
    public ResponseEntity<?> getMyDocumentStatus(
            @PathVariable Long applicationId,
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            Long studentId = extractStudentIdFromToken(token.replace("Bearer ", ""));
            
            GroupMemberDocument document = documentRepository
                    .findByApplicationIdAndStudentId(applicationId, studentId)
                    .orElse(null);

            Map<String, Object> response = new HashMap<>();
            if (document != null) {
                response.put("status", document.getStatus());
                response.put("hasStudentId", document.getStudentIdUrl() != null);
                response.put("hasResume", document.getResumeUrl() != null);
                response.put("uploadedDate", document.getUploadedDate());
            } else {
                response.put("status", "PENDING");
                response.put("hasStudentId", false);
                response.put("hasResume", false);
            }
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    private Long extractStudentIdFromToken(String token) {
        if (token.startsWith("temp-token-")) {
            return Long.parseLong(token.replace("temp-token-", ""));
        }
        throw new RuntimeException("Invalid token format");
    }

    private String saveFile(MultipartFile file, Long studentId, String type) {
        try {
            String uploadPath = uploadDir != null && !uploadDir.isEmpty() ? 
                    uploadDir : System.getProperty("user.home") + "/internhub/uploads";
            Path dirPath = Paths.get(uploadPath).toAbsolutePath();
            
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String fileName = "member" + studentId + "_" + type + "_" + 
                    System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = dirPath.resolve(fileName);
            Files.write(filePath, file.getBytes());
            
            return fileName;
        } catch (Exception e) {
            System.err.println("Error saving file: " + e.getMessage());
            return null;
        }
    }
}
