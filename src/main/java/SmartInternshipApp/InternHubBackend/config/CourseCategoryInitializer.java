package SmartInternshipApp.InternHubBackend.config;

import SmartInternshipApp.InternHubBackend.entity.CompanyCategory;
import SmartInternshipApp.InternHubBackend.entity.CourseCategory;
import SmartInternshipApp.InternHubBackend.repository.CompanyCategoryRepository;
import SmartInternshipApp.InternHubBackend.repository.CourseCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CourseCategoryInitializer implements CommandLineRunner {
    
    @Autowired
    private CourseCategoryRepository courseCategoryRepository;
    
    @Autowired
    private CompanyCategoryRepository companyCategoryRepository;
    
    @Override
    public void run(String... args) {
        if (courseCategoryRepository.count() == 0) {
            List<CompanyCategory> companyCategories = companyCategoryRepository.findAll();
            
            for (CompanyCategory companyCategory : companyCategories) {
                CourseCategory courseCategory = new CourseCategory();
                courseCategory.setName(companyCategory.getName());
                courseCategoryRepository.save(courseCategory);
            }
            
            System.out.println("Migrated " + companyCategories.size() + " categories from company to course categories");
        }
    }
}
