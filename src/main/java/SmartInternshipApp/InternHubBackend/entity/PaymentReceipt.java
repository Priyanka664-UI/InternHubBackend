package SmartInternshipApp.InternHubBackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_receipts")
public class PaymentReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "receipt_number", unique = true, nullable = false)
    private String receiptNumber;
    
    @Column(name = "payment_id", nullable = false)
    private String paymentId;
    
    @Column(name = "order_id")
    private String orderId;
    
    @ManyToOne
    @JoinColumn(name = "application_id")
    private InternshipApplication application;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @Column(name = "amount", nullable = false)
    private Double amount;
    
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;
    
    @Column(name = "payment_method")
    private String paymentMethod;
    
    @Column(name = "company_name")
    private String companyName;
    
    @Column(name = "internship_title")
    private String internshipTitle;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "application_type")
    private ApplicationType applicationType;
    
    @Column(name = "group_id")
    private Long groupId;
    
    public enum ApplicationType {
        INDIVIDUAL, GROUP
    }
    
    // Constructors
    public PaymentReceipt() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }
    
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public InternshipApplication getApplication() { return application; }
    public void setApplication(InternshipApplication application) { this.application = application; }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    
    public String getInternshipTitle() { return internshipTitle; }
    public void setInternshipTitle(String internshipTitle) { this.internshipTitle = internshipTitle; }
    
    public ApplicationType getApplicationType() { return applicationType; }
    public void setApplicationType(ApplicationType applicationType) { this.applicationType = applicationType; }
    
    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }
}
