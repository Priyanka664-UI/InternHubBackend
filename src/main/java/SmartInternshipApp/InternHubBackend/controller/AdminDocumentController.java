package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.InternshipApplication;
import SmartInternshipApp.InternHubBackend.repository.InternshipApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminDocumentController {

    @Autowired
    private InternshipApplicationRepository applicationRepository;

    @Value("${file.upload.dir}")
    private String uploadDir;

    @GetMapping("/documents")
    public ResponseEntity<List<Map<String, Object>>> getAllDocuments(
            @RequestHeader(value = "X-User-Type", required = false) Integer userType,
            @RequestHeader(value = "X-Company-Id", required = false) Long companyId,
            @RequestParam(value = "companyName", required = false) String filterCompanyName) {
        
        System.out.println("=== DOCUMENT REQUEST ===");
        System.out.println("User Type: " + userType);
        System.out.println("Company ID: " + companyId);
        System.out.println("Filter Company Name: " + filterCompanyName);
        
        File folder = new File(uploadDir);
        List<Map<String, Object>> documents = new java.util.ArrayList<>();
        
        if (!folder.exists() || !folder.isDirectory()) {
            return ResponseEntity.ok(documents);
        }

        File[] files = folder.listFiles();
        if (files == null) {
            return ResponseEntity.ok(documents);
        }

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String[] parts = fileName.split("_");
                
                Map<String, Object> doc = new HashMap<>();
                doc.put("fileName", fileName);
                doc.put("size", file.length());
                doc.put("lastModified", file.lastModified());
                
                String companyName = "N/A";
                String studentName = "N/A";
                String internshipTitle = "N/A";
                
                if (parts.length >= 2) {
                    try {
                        if (parts[0].startsWith("app")) {
                            String appIdStr = parts[0].replace("app", "");
                            Long applicationId = Long.parseLong(appIdStr);
                            doc.put("applicationId", applicationId);
                            doc.put("studentId", null);
                            
                            Optional<InternshipApplication> appOpt = applicationRepository.findById(applicationId);
                            if (appOpt.isPresent()) {
                                InternshipApplication app = appOpt.get();
                                if (app.getStudent() != null) {
                                    studentName = app.getStudent().getFullName();
                                }
                                if (app.getInternship() != null) {
                                    internshipTitle = app.getInternship().getTitle();
                                    companyName = app.getInternship().getCompany();
                                }
                            }
                        } else {
                            doc.put("studentId", Long.parseLong(parts[0]));
                            doc.put("applicationId", null);
                        }
                        doc.put("type", parts[1]);
                    } catch (Exception e) {
                        doc.put("applicationId", null);
                        doc.put("studentId", null);
                        doc.put("type", "unknown");
                    }
                }
                
                if (userType != null && userType == 2 && filterCompanyName != null) {
                    System.out.println("Checking filter: '" + companyName + "' vs '" + filterCompanyName + "'");
                    if (!companyName.trim().equalsIgnoreCase(filterCompanyName.trim())) {
                        System.out.println("Skipping document - company mismatch");
                        continue;
                    }
                    System.out.println("Document matches company filter");
                }
                
                doc.put("companyName", companyName);
                doc.put("studentName", studentName);
                doc.put("internshipTitle", internshipTitle);
                
                documents.add(doc);
            }
        }
        
        System.out.println("Returning " + documents.size() + " documents");
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/documents/{fileName}")
    public ResponseEntity<org.springframework.core.io.Resource> viewDocument(@PathVariable String fileName) {
        try {
            File file = new File(uploadDir, fileName);
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            String contentType = "application/pdf";
            if (fileName.toLowerCase().endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (fileName.toLowerCase().endsWith(".png")) {
                contentType = "image/png";
            } else {
                contentType = "application/octet-stream";
            }
            
            org.springframework.core.io.Resource resource = new org.springframework.core.io.FileSystemResource(file);
            return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/documents/{fileName}/download")
    public ResponseEntity<org.springframework.core.io.Resource> downloadDocument(@PathVariable String fileName) {
        try {
            File file = new File(uploadDir, fileName);
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            org.springframework.core.io.Resource resource = new org.springframework.core.io.FileSystemResource(file);
            return ResponseEntity.ok()
                .header("Content-Type", "application/octet-stream")
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}