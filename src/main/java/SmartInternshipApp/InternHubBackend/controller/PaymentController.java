package SmartInternshipApp.InternHubBackend.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import SmartInternshipApp.InternHubBackend.entity.InternshipApplication;
import SmartInternshipApp.InternHubBackend.entity.PaymentReceipt;
import SmartInternshipApp.InternHubBackend.repository.InternshipApplicationRepository;
import SmartInternshipApp.InternHubBackend.service.PaymentReceiptService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;
    
    @Autowired
    private PaymentReceiptService receiptService;
    
    @Autowired
    private InternshipApplicationRepository applicationRepository;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> data) {
        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", data.get("amount"));
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "receipt_" + System.currentTimeMillis());

            Order order = razorpay.orders.create(orderRequest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("key", razorpayKeyId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> data) {
        try {
            String paymentId = data.get("razorpay_payment_id");
            String orderId = data.get("razorpay_order_id");
            Long applicationId = Long.parseLong(data.get("applicationId"));
            Double amount = Double.parseDouble(data.get("amount"));
            
            // Update application payment status
            Optional<InternshipApplication> appOpt = applicationRepository.findById(applicationId);
            if (appOpt.isPresent()) {
                InternshipApplication application = appOpt.get();
                application.setPaymentStatus("COMPLETED");
                application.setPaymentId(paymentId);
                applicationRepository.save(application);
                
                // Create receipt
                PaymentReceipt receipt = receiptService.createReceipt(application, paymentId, orderId, amount / 100);
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Payment verified successfully");
                response.put("receiptNumber", receipt.getReceiptNumber());
                response.put("receiptId", receipt.getId());
                return ResponseEntity.ok(response);
            }
            
            return ResponseEntity.badRequest().body("{\"message\": \"Application not found\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @GetMapping("/receipt/application/{applicationId}")
    public ResponseEntity<?> getReceiptByApplicationId(@PathVariable Long applicationId) {
        List<PaymentReceipt> receipts = receiptService.getReceiptByApplicationId(applicationId);
        if (!receipts.isEmpty()) {
            return ResponseEntity.ok(receipts.get(0));
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/receipt/student/{studentId}")
    public ResponseEntity<?> getReceiptsByStudentId(@PathVariable Long studentId) {
        List<PaymentReceipt> receipts = receiptService.getReceiptsByStudentId(studentId);
        return ResponseEntity.ok(receipts);
    }
    
    @GetMapping("/receipt/group/{groupId}")
    public ResponseEntity<?> getReceiptsByGroupId(@PathVariable Long groupId) {
        List<PaymentReceipt> receipts = receiptService.getReceiptsByGroupId(groupId);
        return ResponseEntity.ok(receipts);
    }
}
