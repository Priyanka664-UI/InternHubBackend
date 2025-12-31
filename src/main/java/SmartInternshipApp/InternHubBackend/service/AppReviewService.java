package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.dto.AppReviewDTO;
import SmartInternshipApp.InternHubBackend.entity.AppReview;
import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.repository.AppReviewRepository;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppReviewService {

    @Autowired
    private AppReviewRepository appReviewRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<AppReviewDTO> getAllReviews() {
        return appReviewRepository.findAllOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AppReviewDTO submitReview(Long studentId, Integer rating, String comment) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Check if student already reviewed
        if (appReviewRepository.existsByStudent(student)) {
            throw new RuntimeException("You have already submitted a review for this app");
        }

        // Create new review
        AppReview review = new AppReview(student, rating, comment);
        AppReview savedReview = appReviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    private AppReviewDTO convertToDTO(AppReview review) {
        return new AppReviewDTO(
                review.getId(),
                review.getStudent().getId(),
                review.getStudent().getFullName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}