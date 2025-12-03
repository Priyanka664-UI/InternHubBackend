package SmartInternshipApp.InternHubBackend.config;

import SmartInternshipApp.InternHubBackend.repository.CompanyRepository;
import SmartInternshipApp.InternHubBackend.repository.CourseRepository;
import SmartInternshipApp.InternHubBackend.repository.InternshipRepository;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
import SmartInternshipApp.InternHubBackend.repository.CompanyCategoryRepository;
import SmartInternshipApp.InternHubBackend.entity.CompanyCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private InternshipRepository internshipRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private CompanyCategoryRepository companyCategoryRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // This will trigger table creation
        companyRepository.count();
        internshipRepository.count();
        studentRepository.count();
        courseRepository.count();
        companyCategoryRepository.count();
        
        // Seed default categories if none exist
        if (companyCategoryRepository.count() == 0) {
            String[] defaultCategories = {
                "Software Development",
                "Artificial Intelligence / Machine Learning",
                "Data Science & Big Data Analytics",
                "Cloud Computing",
                "Cybersecurity",
                "Embedded Systems & IoT",
                "Electronics & Semiconductor",
                "Power & Electrical Systems",
                "Robotics & Automation",
                "Automotive & Manufacturing",
                "Construction & Infrastructure",
                "Structural & Civil Engineering",
                "Telecommunications & Networking"
            };
            
            for (String categoryName : defaultCategories) {
                companyCategoryRepository.save(new CompanyCategory(categoryName));
            }
        }
    }
}