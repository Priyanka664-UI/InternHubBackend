package SmartInternshipApp.InternHubBackend.repository;

import SmartInternshipApp.InternHubBackend.entity.PaymentReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, Long> {
    List<PaymentReceipt> findByPaymentId(String paymentId);
    List<PaymentReceipt> findByStudentId(Long studentId);
    List<PaymentReceipt> findByApplicationId(Long applicationId);
    List<PaymentReceipt> findByGroupId(Long groupId);
}
