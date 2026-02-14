package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.CompanyReview;
import SmartInternshipApp.InternHubBackend.service.CompanyReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class CompanyReviewController {
    @Autowired
    private CompanyReviewService reviewService;

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CompanyReview>> getCompanyReviews(@PathVariable Long companyId) {
        return ResponseEntity.ok(reviewService.getCompanyReviews(companyId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CompanyReview>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/company/{companyId}/stats")
    public ResponseEntity<Map<String, Object>> getCompanyStats(@PathVariable Long companyId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("averageRating", reviewService.getAverageRating(companyId));
        stats.put("reviewCount", reviewService.getReviewCount(companyId));
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/submit")
    public ResponseEntity<CompanyReview> submitReview(
            @RequestParam Long companyId,
            @RequestParam Long studentId,
            @RequestParam Integer rating,
            @RequestParam String description) {
        CompanyReview review = reviewService.submitReview(companyId, studentId, rating, description);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }
}
