package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.dto.AppReviewDTO;
import SmartInternshipApp.InternHubBackend.service.AppReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class AppReviewController {

    @Autowired
    private AppReviewService appReviewService;

    @GetMapping("/app")
    public ResponseEntity<List<AppReviewDTO>> getAllAppReviews() {
        try {
            List<AppReviewDTO> reviews = appReviewService.getAllReviews();
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/app")
    public ResponseEntity<?> submitAppReview(@RequestBody Map<String, Object> request) {
        try {
            Long studentId = Long.valueOf(request.get("studentId").toString());
            Integer rating = Integer.valueOf(request.get("rating").toString());
            String comment = request.get("comment").toString();

            if (rating < 1 || rating > 5) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Rating must be between 1 and 5"));
            }

            if (comment == null || comment.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Comment is required"));
            }

            AppReviewDTO review = appReviewService.submitReview(studentId, rating, comment.trim());
            return ResponseEntity.ok(review);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to submit review"));
        }
    }
}