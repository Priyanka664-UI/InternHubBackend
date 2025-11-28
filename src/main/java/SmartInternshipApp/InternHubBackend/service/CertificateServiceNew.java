package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.*;
import SmartInternshipApp.InternHubBackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CertificateServiceNew {
    
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
    
    public Map<String, Object> generateAICertificate(Map<String, Object> request) {
        String studentName = (String) request.get("studentName");
        String studentEmail = (String) request.get("studentEmail");
        String college = (String) request.get("college");
        String internshipTitle = (String) request.get("internshipTitle");
        String companyName = (String) request.get("companyName");
        String startDate = (String) request.get("startDate");
        String endDate = (String) request.get("endDate");
        String template = (String) request.get("template");
        String customPrompt = (String) request.get("customPrompt");
        
        String certificateNumber = "AI-CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        String certificateContent = generateCertificateContent(studentName, studentEmail, college, 
            internshipTitle, companyName, startDate, endDate, template, customPrompt, certificateNumber);
        
        Map<String, Object> result = new HashMap<>();
        result.put("certificateNumber", certificateNumber);
        result.put("content", certificateContent);
        result.put("studentName", studentName);
        result.put("internshipTitle", internshipTitle);
        result.put("companyName", companyName);
        
        return result;
    }
    
    private String generateCertificateContent(String studentName, String studentEmail, String college,
            String internshipTitle, String companyName, String startDate, String endDate, 
            String template, String customPrompt, String certificateNumber) {
        
        StringBuilder content = new StringBuilder();
        String cssStyle = getCSSForTemplate(template);
        
        content.append("<!DOCTYPE html><html><head>");
        content.append("<meta charset='UTF-8'>");
        content.append("<title>Certificate of Completion</title>");
        content.append("<style>").append(cssStyle).append("</style>");
        content.append("</head><body>");
        
        content.append("<div class='certificate-container'>");
        content.append("<div class='certificate-header'>");
        content.append("<h1>CERTIFICATE OF COMPLETION</h1>");
        content.append("</div>");
        
        content.append("<div class='certificate-body'>");
        content.append("<p class='intro'>This is to certify that</p>");
        content.append("<div class='student-name'>").append(studentName).append("</div>");
        content.append("<p class='college'>from ").append(college).append("</p>");
        content.append("<p class='completion'>has successfully completed the internship program</p>");
        content.append("<div class='internship-title'>").append(internshipTitle).append("</div>");
        content.append("<p class='company'>at ").append(companyName).append("</p>");
        content.append("<p class='duration'>Duration: ").append(startDate).append(" to ").append(endDate).append("</p>");
        
        if (customPrompt != null && !customPrompt.trim().isEmpty()) {
            content.append("<p class='custom-note'>").append(customPrompt).append("</p>");
        }
        
        content.append("<p class='recognition'>We recognize their dedication, hard work, and valuable contributions during this internship period.</p>");
        content.append("</div>");
        
        content.append("<div class='certificate-footer'>");
        content.append("<div class='signature-section'>");
        content.append("<div class='signature'>");
        content.append("<p>_________________________</p>");
        content.append("<p>Authorized Signature</p>");
        content.append("</div>");
        content.append("<div class='date'>");
        content.append("<p>Date: ").append(java.time.LocalDate.now()).append("</p>");
        content.append("</div>");
        content.append("</div>");
        content.append("<p class='certificate-number'>Certificate No: ").append(certificateNumber).append("</p>");
        content.append("</div>");
        
        content.append("</div></body></html>");
        
        return content.toString();
    }
    
    private String getCSSForTemplate(String template) {
        return "body { margin: 0; padding: 20px; background: linear-gradient(45deg, #dc2626, #16a34a); font-family: Arial, sans-serif; } " +
               ".certificate-container { max-width: 800px; margin: 0 auto; padding: 60px; background: white; border: 10px solid #dc2626; border-radius: 20px; box-shadow: 0 20px 40px rgba(0,0,0,0.3); position: relative; } " +
               ".certificate-container::before { content: ''; position: absolute; top: 15px; left: 15px; right: 15px; bottom: 15px; border: 5px solid #16a34a; border-radius: 10px; } " +
               ".certificate-header h1 { text-align: center; font-size: 48px; font-weight: bold; margin: 40px 0; background: linear-gradient(45deg, #dc2626, #16a34a); -webkit-background-clip: text; -webkit-text-fill-color: transparent; letter-spacing: 3px; } " +
               ".certificate-body { text-align: center; line-height: 2; } " +
               ".intro { font-size: 24px; color: #374151; margin: 20px 0; } " +
               ".student-name { font-size: 42px; font-weight: bold; color: #dc2626; margin: 30px 0; text-transform: uppercase; letter-spacing: 2px; } " +
               ".college { font-size: 20px; color: #16a34a; margin: 15px 0; font-weight: bold; } " +
               ".completion { font-size: 22px; color: #374151; margin: 25px 0; } " +
               ".internship-title { font-size: 32px; font-weight: bold; color: #16a34a; margin: 25px 0; font-style: italic; } " +
               ".company { font-size: 20px; color: #374151; margin: 15px 0; font-weight: bold; } " +
               ".duration { font-size: 18px; color: #6b7280; margin: 20px 0; } " +
               ".recognition { font-size: 18px; color: #374151; margin: 30px 0; font-style: italic; } " +
               ".signature-section { display: flex; justify-content: space-between; margin-top: 60px; padding-top: 30px; border-top: 3px solid #dc2626; } " +
               ".signature p { margin: 5px 0; color: #374151; font-weight: bold; } " +
               ".date p { color: #6b7280; } " +
               ".certificate-number { text-align: center; font-size: 16px; color: #9ca3af; margin-top: 30px; font-weight: bold; }";
    }
}