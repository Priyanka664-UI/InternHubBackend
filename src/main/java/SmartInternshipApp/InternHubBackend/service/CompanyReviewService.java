package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.CompanyReview;
import SmartInternshipApp.InternHubBackend.entity.Company;
import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.dto.ReviewDTO;
import SmartInternshipApp.InternHubBackend.repository.CompanyReviewRepository;
import SmartInternshipApp.InternHubBackend.repository.CompanyRepository;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyReviewService {
    @Autowired
    private CompanyReviewRepository reviewRepository;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private StudentRepository studentRepository;

    public List<CompanyReview> getCompanyReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    public List<CompanyReview> getAllReviews() {
        return reviewRepository.findAll();
    }

    public CompanyReview submitReview(Long companyId, Long studentId, Integer rating, String description) {
        Company company = companyRepository.findById(companyId).orElseThrow();
        Student student = studentRepository.findById(studentId).orElseThrow();
        
        CompanyReview review = new CompanyReview();
        review.setCompany(company);
        review.setStudent(student);
        review.setRating(rating);
        review.setDescription(description);
        
        return reviewRepository.save(review);
    }

    public Double getAverageRating(Long companyId) {
        Double avg = reviewRepository.getAverageRating(companyId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    public Long getReviewCount(Long companyId) {
        return reviewRepository.getReviewCount(companyId);
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public List<ReviewDTO> getCompanyReviewsDTO(Long companyId) {
        return reviewRepository.findByCompanyId(companyId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<ReviewDTO> getAllReviewsDTO() {
        return reviewRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private ReviewDTO convertToDTO(CompanyReview review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setDescription(review.getDescription());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setCompany(new ReviewDTO.CompanyInfo(review.getCompany().getId(), review.getCompany().getName()));
        dto.setStudent(new ReviewDTO.StudentInfo(review.getStudent().getId(), review.getStudent().getFullName()));
        return dto;
    }
}
