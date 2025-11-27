package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Certificate;
import SmartInternshipApp.InternHubBackend.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/certificates")
@CrossOrigin(origins = "*")
public class AdminCertificateController {
    
    @Autowired
    private CertificateService certificateService;
    
    @PostMapping("/issue")
    public ResponseEntity<Certificate> issueCertificate(@RequestBody Map<String, Object> request) {
        Long studentId = Long.valueOf(request.get("studentId").toString());
        Long internshipId = Long.valueOf(request.get("internshipId").toString());
        Integer rating = Integer.valueOf(request.get("rating").toString());
        String remarks = request.get("remarks").toString();
        
        Certificate certificate = certificateService.issueCertificate(studentId, internshipId, rating, remarks);
        return ResponseEntity.ok(certificate);
    }
    
    @GetMapping
    public ResponseEntity<List<Certificate>> getAllCertificates() {
        return ResponseEntity.ok(certificateService.getAllCertificates());
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Certificate>> getCertificatesByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(certificateService.getCertificatesByStudent(studentId));
    }
    
    @GetMapping("/verify/{certificateNumber}")
    public ResponseEntity<Certificate> verifyCertificate(@PathVariable String certificateNumber) {
        return ResponseEntity.ok(certificateService.getCertificateByNumber(certificateNumber));
    }
    
    @DeleteMapping("/{certificateId}")
    public ResponseEntity<String> deleteCertificate(@PathVariable Long certificateId) {
        certificateService.deleteCertificate(certificateId);
        return ResponseEntity.ok("Certificate deleted successfully");
    }
}