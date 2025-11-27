package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.*;
import SmartInternshipApp.InternHubBackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CertificateService {
    
    @Autowired private CertificateRepository certificateRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private InternshipRepository internshipRepository;
    
    public Certificate issueCertificate(Long studentId, Long internshipId, Integer rating, String remarks) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        Internship internship = internshipRepository.findById(internshipId)
            .orElseThrow(() -> new RuntimeException("Internship not found"));
        
        Certificate certificate = new Certificate();
        certificate.setStudent(student);
        certificate.setInternship(internship);
        certificate.setCertificateNumber("CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        certificate.setCompletionDate(LocalDateTime.now());
        certificate.setPerformanceRating(rating);
        certificate.setRemarks(remarks);
        
        return certificateRepository.save(certificate);
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