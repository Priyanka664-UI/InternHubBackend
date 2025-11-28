package SmartInternshipApp.InternHubBackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/admin/certificates")
@CrossOrigin(originPatterns = "*")
public class AdminCertificateController {
    
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
    public ResponseEntity<List<Object>> getAllCertificates() {
        return ResponseEntity.ok(new ArrayList<>());
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
    public ResponseEntity<Object> issueCertificate(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(new HashMap<>());
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
            "<div style='width:800px;height:600px;margin:20px auto;background:white;border:10px solid #000;box-shadow:0 0 30px rgba(0,0,0,0.3);position:relative;'>" +
            "<div style='position:absolute;top:15px;left:15px;right:15px;bottom:15px;border:3px solid #333;background:white;padding:30px;box-sizing:border-box;'>" +
            "<div style='text-align:center;height:100%%;display:flex;flex-direction:column;justify-content:space-between;'>" +
            "<div style='flex-grow:1;display:flex;flex-direction:column;justify-content:center;'>" +
            "<div style='font-family:Georgia,serif;font-size:18px;color:#666;letter-spacing:4px;margin-bottom:15px;'>CERTIFICATE OF</div>" +
            "<h1 style='font-family:Georgia,serif;font-size:38px;color:#000;margin:0 0 15px 0;font-weight:bold;'>EXCELLENCE</h1>" +
            "<div style='width:150px;height:2px;background:#000;margin:0 auto 20px auto;'></div>" +
            "<div style='font-family:Georgia,serif;font-size:18px;color:#333;margin-bottom:15px;'>This is to certify that</div>" +
            "<div style='font-family:Georgia,serif;font-size:28px;color:#000;margin:10px 0;font-weight:bold;text-transform:uppercase;border-bottom:3px solid #000;padding-bottom:8px;display:inline-block;'>%s</div>" +
            "<div style='font-family:Georgia,serif;font-size:16px;color:#333;margin:15px 0;'>has successfully completed the internship program</div>" +
            "<div style='border:2px solid #000;color:#000;padding:10px 20px;margin:10px auto;font-family:Georgia,serif;font-size:18px;font-weight:bold;display:inline-block;'>%s</div>" +
            "<div style='font-family:Georgia,serif;font-size:16px;color:#333;margin:8px 0;'>at</div>" +
            "<div style='background:#000;color:white;padding:10px 20px;margin:10px auto;font-family:Georgia,serif;font-size:18px;font-weight:bold;display:inline-block;'>%s</div>" +
            "</div>" +
            "<div style='display:flex;justify-content:space-between;font-family:Georgia,serif;border-top:2px solid #000;padding-top:15px;margin-top:15px;'>" +
            "<div style='text-align:left;'><div style='font-size:12px;color:#666;margin-bottom:3px;'>Date of Completion</div><div style='font-size:14px;font-weight:bold;color:#000;'>%s</div></div>" +
            "<div style='text-align:right;'><div style='font-size:12px;color:#666;margin-bottom:3px;'>Certificate Number</div><div style='font-size:14px;font-weight:bold;color:#000;'>%s</div></div>" +
            "</div>" +
            "</div></div></div>",
            studentName, internshipTitle, companyName, date, certificateNumber
        );
    }
}