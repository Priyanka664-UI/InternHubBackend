package SmartInternshipApp.InternHubBackend.dto;

import java.time.LocalDateTime;

public class ReviewDTO {
    private Long id;
    private Integer rating;
    private String description;
    private LocalDateTime createdAt;
    private CompanyInfo company;
    private StudentInfo student;

    public static class CompanyInfo {
        private Long id;
        private String name;

        public CompanyInfo(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class StudentInfo {
        private Long id;
        private String fullName;

        public StudentInfo(Long id, String fullName) {
            this.id = id;
            this.fullName = fullName;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public CompanyInfo getCompany() { return company; }
    public void setCompany(CompanyInfo company) { this.company = company; }
    public StudentInfo getStudent() { return student; }
    public void setStudent(StudentInfo student) { this.student = student; }
}
