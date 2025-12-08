package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.InternshipApplication;
import SmartInternshipApp.InternHubBackend.entity.Certificate;
import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.repository.InternshipApplicationRepository;
import SmartInternshipApp.InternHubBackend.repository.CertificateRepository;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/student/documents")
public class StudentDocumentController {

    @Autowired
    private InternshipApplicationRepository applicationRepository;
    
    @Autowired
    private CertificateRepository certificateRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @org.springframework.beans.factory.annotation.Value("${file.certificate.dir}")
    private String certificateDir;
    
    private static final String HTML_INTERNSHIP_START = "font-style:italic;'>";
    private static final String HTML_COMPANY_START = ">at ";
    private static final String HTML_DIV_END = "</div>";
    private static final String CERT_FILE_EXT = ".html";
    private static final String TOKEN_PREFIX = "temp-token-";

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Controller is working");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/applications")
    public ResponseEntity<?> getStudentApplications(@RequestHeader("Authorization") String token) {
        try {
            System.out.println("Token received: " + token);
            Long studentId = extractStudentIdFromToken(token);
            System.out.println("Student ID: " + studentId);
            
            Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
            System.out.println("Student found: " + student.getFullName());
            
            List<InternshipApplication> applications = applicationRepository.findByStudent(student);
            System.out.println("Applications found: " + applications.size());
            
            List<Map<String, Object>> result = new ArrayList<>();
            
            for (InternshipApplication app : applications) {
                try {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", app.getId());
                    data.put("internshipTitle", app.getInternship() != null ? app.getInternship().getTitle() : "N/A");
                    data.put("companyName", app.getInternship() != null ? app.getInternship().getCompany() : "N/A");
                    data.put("status", app.getStatus().toString());
                    data.put("appliedDate", app.getAppliedDate().toString());
                    data.put("college", app.getCollege());
                    data.put("degree", app.getDegree());
                    data.put("yearOfStudy", app.getYearOfStudy());
                    data.put("studentIdUrl", app.getStudentIdUrl() != null ? "/api/files/applications/" + app.getStudentIdUrl() : null);
                    data.put("resumeUrl", app.getResumeUrl() != null ? "/api/files/applications/" + app.getResumeUrl() : null);
                    result.add(data);
                } catch (Exception ex) {
                    System.err.println("Error processing application " + app.getId() + ": " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("ERROR in getStudentApplications: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("type", e.getClass().getName());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/certificates")
    public ResponseEntity<?> getStudentCertificates(@RequestHeader("Authorization") String token) {
        try {
            Long studentId = extractStudentIdFromToken(token);
            Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
            
            System.out.println("Looking for certificates for: " + student.getFullName());
            System.out.println("Certificate directory: " + certificateDir);
            
            java.io.File dir = new java.io.File(certificateDir);
            List<Map<String, Object>> result = new ArrayList<>();
            
            if (dir.exists()) {
                System.out.println("Directory exists");
                java.io.File[] files = dir.listFiles((d, name) -> name.endsWith(CERT_FILE_EXT));
                System.out.println("Found " + (files != null ? files.length : 0) + " HTML files");
                
                if (files != null) {
                    for (java.io.File file : files) {
                        String fileName = file.getName();
                        System.out.println("Processing file: " + fileName);
                        
                        String studentNameInFile = fileName.split("_", 2)[1].replace(CERT_FILE_EXT, "").replace("_", " ");
                        System.out.println("Student name in file: '" + studentNameInFile + "'");
                        System.out.println("Logged in student: '" + student.getFullName() + "'");
                        
                        String studentLower = student.getFullName().toLowerCase();
                        String fileLower = studentNameInFile.toLowerCase();
                        
                        // Try exact match first
                        boolean matches = studentLower.contains(fileLower) || fileLower.contains(studentLower);
                        
                        // If no match, try similarity check (80% similar)
                        if (!matches) {
                            String studentClean = studentLower.replaceAll("[^a-z]", "");
                            String fileClean = fileLower.replaceAll("[^a-z]", "");
                            int similarity = calculateSimilarity(studentClean, fileClean);
                            matches = similarity >= 80;
                            System.out.println("Similarity: " + similarity + "%");
                        }
                        
                        System.out.println("Match: " + matches);
                        
                        if (matches) {
                            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
                            String certNumber = fileName.split("_")[0];
                            String internshipTitle = extractBetween(content, HTML_INTERNSHIP_START, HTML_DIV_END);
                            String companyName = extractBetween(content, HTML_COMPANY_START, HTML_DIV_END);
                            System.out.println("MATCH FOUND! Extracted - Title: " + internshipTitle + ", Company: " + companyName);
                            
                            Map<String, Object> data = new HashMap<>();
                            data.put("certificateNumber", certNumber);
                            data.put("studentName", student.getFullName());
                            data.put("internshipTitle", internshipTitle);
                            data.put("companyName", companyName);
                            data.put("fileName", fileName);
                            result.add(data);
                        }
                    }
                }
            } else {
                System.out.println("Directory does not exist: " + certificateDir);
            }
            
            System.out.println("Returning " + result.size() + " certificates");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ArrayList<>());
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
    
    private int calculateSimilarity(String s1, String s2) {
        int longer = Math.max(s1.length(), s2.length());
        if (longer == 0) return 100;
        int editDistance = computeLevenshteinDistance(s1, s2);
        return (int) (((longer - editDistance) / (double) longer) * 100.0);
    }
    
    private int computeLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        return dp[s1.length()][s2.length()];
    }
    
    @GetMapping("/certificates/{fileName}")
    public ResponseEntity<String> getCertificateContent(@PathVariable String fileName) {
        try {
            java.nio.file.Path filePath = java.nio.file.Paths.get(certificateDir, fileName);
            String content = new String(java.nio.file.Files.readAllBytes(filePath));
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private Long extractStudentIdFromToken(String token) {
        String cleanToken = token.replace("Bearer ", "");
        if (cleanToken.startsWith(TOKEN_PREFIX)) {
            return Long.parseLong(cleanToken.replace(TOKEN_PREFIX, ""));
        }
        throw new RuntimeException("Invalid token format");
    }
}
