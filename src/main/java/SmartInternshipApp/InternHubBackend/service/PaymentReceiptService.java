package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.InternshipApplication;
import SmartInternshipApp.InternHubBackend.entity.PaymentReceipt;
import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.repository.PaymentReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentReceiptService {
    
    @Autowired
    private PaymentReceiptRepository receiptRepository;
    
    public PaymentReceipt createReceipt(InternshipApplication application, String paymentId, String orderId, Double amount) {
        PaymentReceipt receipt = new PaymentReceipt();
        receipt.setReceiptNumber(generateReceiptNumber());
        receipt.setPaymentId(paymentId);
        receipt.setOrderId(orderId);
        receipt.setApplication(application);
        receipt.setStudent(application.getStudent());
        receipt.setAmount(amount);
        receipt.setPaymentDate(LocalDateTime.now());
        receipt.setPaymentMethod("Razorpay");
        receipt.setCompanyName(application.getInternship().getCompany());
        receipt.setInternshipTitle(application.getInternship().getTitle());
        receipt.setApplicationType(application.getApplicationType() == InternshipApplication.ApplicationType.GROUP 
            ? PaymentReceipt.ApplicationType.GROUP 
            : PaymentReceipt.ApplicationType.INDIVIDUAL);
        
        if (application.getGroup() != null) {
            receipt.setGroupId(application.getGroup().getId());
        }
        
        return receiptRepository.save(receipt);
    }
    
    public List<PaymentReceipt> getReceiptByPaymentId(String paymentId) {
        return receiptRepository.findByPaymentId(paymentId);
    }
    
    public List<PaymentReceipt> getReceiptsByStudentId(Long studentId) {
        return receiptRepository.findByStudentId(studentId);
    }
    
    public List<PaymentReceipt> getReceiptByApplicationId(Long applicationId) {
        return receiptRepository.findByApplicationId(applicationId);
    }
    
    public List<PaymentReceipt> getReceiptsByGroupId(Long groupId) {
        return receiptRepository.findByGroupId(groupId);
    }
    
    private String generateReceiptNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        int random = (int)(Math.random() * 1000);
        return "RCP" + timestamp + random;
    }
}
