package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Certificate;
import SmartInternshipApp.InternHubBackend.service.CertificateService;
import SmartInternshipApp.InternHubBackend.service.CertificatePdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/certificates")
public class AdminCertificateController {
    
    @Autowired private CertificateService certificateService;
    @Autowired private CertificatePdfService pdfService;
    
    @Value("${file.upload.dir}")
    private String uploadDir;
    
    private static final Map<String, Map<String, Object>> certificateStore = new ConcurrentHashMap<>();
    
    // Initialize with test certificate
    static {
        Map<String, Object> testCert = new HashMap<>();
        testCert.put("certificateNumber", "C05320C6");
        testCert.put("student", Map.of("name", "John Doe", "email", "john@example.com", "college", "Test College"));
        testCert.put("internship", Map.of("title", "Software Development", "company", "Tech Corp"));
        testCert.put("issueDate", "December 15, 2024");
        testCert.put("performanceRating", 5);
        certificateStore.put("C05320C6", testCert);
    }
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllCertificates() {
        List<Certificate> certificates = certificateService.getAllCertificates();
        List<Map<String, Object>> response = certificates.stream().map(cert -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cert.getId());
            map.put("certificateNumber", cert.getCertificateNumber());
            map.put("studentName", cert.getStudent().getFullName());
            map.put("studentEmail", cert.getStudent().getEmail());
            map.put("internshipTitle", cert.getInternship().getTitle());
            map.put("companyName", cert.getInternship().getCompany());
            map.put("issueDate", cert.getIssueDate());
            map.put("completionDate", cert.getCompletionDate());
            map.put("performanceRating", cert.getPerformanceRating());
            map.put("certificateUrl", cert.getCertificateUrl());
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Object>> getCertificatesByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(new ArrayList<>());
    }
    
    @GetMapping("/verify/{certificateNumber}")
    public ResponseEntity<Object> verifyCertificate(@PathVariable String certificateNumber) {
        System.out.println("Verifying certificate: " + certificateNumber);
        System.out.println("Available certificates: " + certificateStore.keySet());
        
        // Try exact match first
        Map<String, Object> certificate = certificateStore.get(certificateNumber);
        
        // If not found, try with CERT- prefix
        if (certificate == null && !certificateNumber.startsWith("CERT-")) {
            certificate = certificateStore.get("CERT-" + certificateNumber);
        }
        
        // If still not found, try without CERT- prefix
        if (certificate == null && certificateNumber.startsWith("CERT-")) {
            certificate = certificateStore.get(certificateNumber.substring(5));
        }
        
        if (certificate != null) {
            return ResponseEntity.ok(certificate);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/test-store")
    public ResponseEntity<Object> testStore() {
        return ResponseEntity.ok(certificateStore);
    }
    
    @PostMapping("/issue")
    public ResponseEntity<Map<String, Object>> issueCertificate(@RequestBody Map<String, Object> request) {
        Long studentId = Long.valueOf(request.get("studentId").toString());
        Long internshipId = Long.valueOf(request.get("internshipId").toString());
        Integer rating = request.get("rating") != null ? Integer.valueOf(request.get("rating").toString()) : 5;
        String remarks = (String) request.get("remarks");
        
        Certificate certificate = certificateService.issueCertificate(studentId, internshipId, rating, remarks);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", certificate.getId());
        response.put("certificateNumber", certificate.getCertificateNumber());
        response.put("certificateUrl", certificate.getCertificateUrl());
        response.put("message", "Certificate issued successfully");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/download/{certificateNumber}")
    public ResponseEntity<Resource> downloadCertificate(@PathVariable String certificateNumber) {
        try {
            String certDir = uploadDir.replace("applications", "certificates");
            File pdfFile = new File(certDir, certificateNumber + ".pdf");
            
            if (!pdfFile.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = new FileSystemResource(pdfFile);
            
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + certificateNumber + ".pdf\"")
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/{certificateId}")
    public ResponseEntity<String> deleteCertificate(@PathVariable Long certificateId) {
        return ResponseEntity.ok("Certificate deleted successfully");
    }
    
    @PostMapping("/generate-ai")
    public ResponseEntity<Map<String, Object>> generateAICertificate(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        String studentName = (String) request.get("studentName");
        String internshipTitle = (String) request.get("internshipTitle");
        String companyName = (String) request.get("companyName");
        String template = (String) request.get("template");
        
        String certificateNumber = "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        
        String certificateContent = generateCertificateHTML(studentName, internshipTitle, companyName, template, certificateNumber, currentDate);
        
        response.put("certificateNumber", certificateNumber);
        response.put("content", certificateContent);
        response.put("generatedAt", currentDate);
        
        // Store certificate for verification
        Map<String, Object> certificateData = new HashMap<>();
        certificateData.put("certificateNumber", certificateNumber);
        certificateData.put("student", Map.of("name", studentName));
        certificateData.put("internship", Map.of("title", internshipTitle, "company", companyName));
        certificateData.put("issueDate", currentDate);
        certificateData.put("performanceRating", 5);
        certificateStore.put(certificateNumber, certificateData);
        
        return ResponseEntity.ok(response);
    }
    
    private String generateCertificateHTML(String studentName, String internshipTitle, String companyName, String template, String certificateNumber, String date) {
        return String.format(
            "<div style='width:800px;height:600px;margin:40px auto;background:white;border:15px solid #e74c3c;position:relative;box-shadow:0 0 50px rgba(0,0,0,0.5);font-family:Times New Roman,serif;'>" +
            "<div style='position:absolute;top:15px;left:15px;right:15px;bottom:15px;border:8px solid #27ae60;'></div>" +
            "<div style='padding:60px 40px;text-align:center;height:calc(100%% - 120px);display:flex;flex-direction:column;justify-content:center;position:relative;z-index:2;'>" +
            "<div style='font-size:42px;font-weight:bold;color:#e74c3c;margin-bottom:30px;letter-spacing:3px;'>CERTIFICATE OF ACHIEVEMENT</div>" +
            "<div style='font-size:20px;color:#34495e;margin-bottom:25px;'>This is to certify that</div>" +
            "<div style='font-size:36px;font-weight:bold;color:#27ae60;margin:20px 0;text-transform:uppercase;border-bottom:3px solid #e74c3c;padding-bottom:10px;display:inline-block;'>%s</div>" +
            "<div style='font-size:18px;color:#2c3e50;margin:15px 0;'>from University</div>" +
            "<div style='font-size:18px;color:#2c3e50;margin:15px 0;line-height:1.6;'>has successfully completed the internship program</div>" +
            "<div style='font-size:24px;font-weight:bold;color:#e74c3c;margin:20px 0;font-style:italic;'>%s</div>" +
            "<div style='font-size:20px;color:#27ae60;font-weight:bold;margin:15px 0;'>at %s</div>" +
            "<div style='font-size:18px;color:#2c3e50;margin:15px 0;line-height:1.6;'>Duration: January 2024 to March 2024</div>" +
            "</div>" +
            "<div style='position:absolute;bottom:30px;left:40px;right:40px;display:flex;justify-content:space-between;border-top:2px solid #34495e;padding-top:20px;'>" +
            "<div style='text-align:left;font-size:14px;color:#7f8c8d;'><div>_____________________</div><div>Authorized Signature</div></div>" +
            "<div style='text-align:right;font-size:14px;color:#7f8c8d;'><div>%s</div><div>Issue Date</div></div>" +
            "</div>" +
            "<div style='position:absolute;bottom:10px;left:50%%;transform:translateX(-50%%);font-size:12px;color:#95a5a6;font-weight:bold;'>Certificate No: %s</div>" +
            "</div>",
            studentName, internshipTitle, companyName, date, certificateNumber
        );
    }
}