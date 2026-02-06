package SmartInternshipApp.InternHubBackend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.*;

@RestController
@RequestMapping("/api/admin/certificate-files")
public class CertificateFileController {
    
    @Value("${file.certificate.dir}")
    private String CERT_DIR;
    
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveCertificateFile(@RequestBody Map<String, Object> request) {
        try {
            String certificateNumber = (String) request.get("certificateNumber");
            String studentName = (String) request.get("studentName");
            String content = (String) request.get("content");
            
            String fileName = certificateNumber + "_" + studentName.replace(" ", "_") + ".html";
            File dir = new File(CERT_DIR);
            if (!dir.exists()) dir.mkdirs();
            File file = new File(dir, fileName);
            
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("fileName", fileName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> listCertificates(
            @RequestHeader(value = "X-User-Type", required = false) Integer userType,
            @RequestHeader(value = "X-Company-Id", required = false) Long companyId,
            @RequestParam(value = "companyName", required = false) String filterCompanyName) {
        try {
            System.out.println("Certificate directory: " + CERT_DIR);
            System.out.println("User Type: " + userType);
            System.out.println("Company ID: " + companyId);
            
            File dir = new File(CERT_DIR);
            System.out.println("Directory exists: " + dir.exists());
            if (!dir.exists()) return ResponseEntity.ok(new ArrayList<>());
            
            File[] files = dir.listFiles((d, name) -> name.endsWith(".html"));
            System.out.println("Found files: " + (files != null ? files.length : 0));
            
            List<Map<String, Object>> certificates = new ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    String[] parts = fileName.replace(".html", "").split("_", 2);
                    String content = new String(Files.readAllBytes(file.toPath()));
                    
                    String internshipTitle = extractBetween(content, "font-style:italic;'>", "</div>");
                    String companyName = extractBetween(content, ">at ", "</div>");
                    
                    System.out.println("Processing: " + fileName);
                    System.out.println("Internship: " + internshipTitle);
                    System.out.println("Company: " + companyName);
                    
                    // Filter by company if user_type = 2
                    if (userType != null && userType == 2 && filterCompanyName != null) {
                        if (!companyName.equalsIgnoreCase(filterCompanyName)) {
                            System.out.println("Skipping - company mismatch: " + companyName + " != " + filterCompanyName);
                            continue;
                        }
                    }
                    
                    Map<String, Object> cert = new HashMap<>();
                    cert.put("certificateNumber", parts[0]);
                    cert.put("studentName", parts.length > 1 ? parts[1].replace("_", " ") : "Unknown");
                    cert.put("internshipTitle", internshipTitle);
                    cert.put("companyName", companyName);
                    cert.put("fileName", fileName);
                    cert.put("generatedAt", new Date(file.lastModified()));
                    cert.put("content", content);
                    certificates.add(cert);
                }
            }
            
            System.out.println("Returning " + certificates.size() + " certificates");
            return ResponseEntity.ok(certificates);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ArrayList<>());
        }
    }
    
    private String extractBetween(String html, String start, String end) {
        try {
            int startIdx = html.indexOf(start);
            if (startIdx == -1) return "N/A";
            startIdx += start.length();
            int endIdx = html.indexOf(end, startIdx);
            if (endIdx == -1) return "N/A";
            return html.substring(startIdx, endIdx).trim();
        } catch (Exception e) {
            return "N/A";
        }
    }
    
    @GetMapping("/read/{fileName}")
    public ResponseEntity<String> readCertificate(@PathVariable String fileName) {
        try {
            File file = new File(CERT_DIR, fileName);
            String content = new String(Files.readAllBytes(file.toPath()));
            return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")
                .body(content);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/download/{fileName}")
    public ResponseEntity<org.springframework.core.io.Resource> downloadCertificate(@PathVariable String fileName) {
        try {
            File file = new File(CERT_DIR, fileName);
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            org.springframework.core.io.Resource resource = new org.springframework.core.io.FileSystemResource(file);
            return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
