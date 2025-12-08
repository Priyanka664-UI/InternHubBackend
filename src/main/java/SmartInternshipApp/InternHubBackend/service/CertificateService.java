package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.*;
import SmartInternshipApp.InternHubBackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CertificateService {
    
    @Autowired private CertificateRepository certificateRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private InternshipRepository internshipRepository;
    @Autowired private CertificatePdfService pdfService;
    
    @Value("${file.upload.dir}")
    private String uploadDir;
    
    public Certificate issueCertificate(Long studentId, Long internshipId, Integer rating, String remarks) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        Internship internship = internshipRepository.findById(internshipId)
            .orElseThrow(() -> new RuntimeException("Internship not found"));
        
        String certNumber = "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        LocalDateTime completionDate = LocalDateTime.now();
        
        // Generate PDF
        byte[] pdfBytes = pdfService.generateCertificatePdf(
            student.getFullName(),
            internship.getTitle(),
            internship.getCompany(),
            certNumber,
            completionDate
        );
        
        // Save PDF
        String pdfPath = savePdf(pdfBytes, certNumber);
        
        Certificate certificate = new Certificate();
        certificate.setStudent(student);
        certificate.setInternship(internship);
        certificate.setCertificateNumber(certNumber);
        certificate.setCompletionDate(completionDate);
        certificate.setPerformanceRating(rating);
        certificate.setRemarks(remarks);
        certificate.setCertificateUrl(pdfPath);
        
        return certificateRepository.save(certificate);
    }
    
    private String savePdf(byte[] pdfBytes, String certNumber) {
        try {
            File certDir = new File(uploadDir.replace("applications", "certificates"));
            if (!certDir.exists()) certDir.mkdirs();
            
            String fileName = certNumber + ".pdf";
            File pdfFile = new File(certDir, fileName);
            
            try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
                fos.write(pdfBytes);
            }
            
            return "/certificates/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save PDF", e);
        }
    }
    
    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }
    
    public List<Certificate> getCertificatesByStudent(Long studentId) {
        return certificateRepository.findByStudentId(studentId);
    }
    
    public Certificate getCertificateByNumber(String certificateNumber) {
        return certificateRepository.findByCertificateNumber(certificateNumber)
            .orElseThrow(() -> new RuntimeException("Certificate not found"));
    }
    
    public void deleteCertificate(Long certificateId) {
        certificateRepository.deleteById(certificateId);
    }
}