package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.PaymentReceipt;
import SmartInternshipApp.InternHubBackend.service.PaymentReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/receipts")
public class PaymentReceiptController {

    @Autowired
    private PaymentReceiptService receiptService;

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<?> getReceiptByApplicationId(@PathVariable Long applicationId) {
        return receiptService.getReceiptByApplicationId(applicationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getReceiptsByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(receiptService.getReceiptsByStudentId(studentId));
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<?> getReceiptByPaymentId(@PathVariable String paymentId) {
        return receiptService.getReceiptByPaymentId(paymentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
