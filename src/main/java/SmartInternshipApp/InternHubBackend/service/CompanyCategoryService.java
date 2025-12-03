package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.CompanyCategory;
import SmartInternshipApp.InternHubBackend.repository.CompanyCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyCategoryService {
    
    @Autowired
    private CompanyCategoryRepository companyCategoryRepository;
    
    public List<CompanyCategory> getAllCategories() {
        return companyCategoryRepository.findAll();
    }
    
    public Optional<CompanyCategory> getCategoryById(Long id) {
        return companyCategoryRepository.findById(id);
    }
    
    public CompanyCategory createCategory(CompanyCategory category) {
        return companyCategoryRepository.save(category);
    }
    
    public CompanyCategory updateCategory(Long id, CompanyCategory category) {
        if (companyCategoryRepository.existsById(id)) {
            category.setId(id);
            return companyCategoryRepository.save(category);
        }
        return null;
    }
    
    public boolean deleteCategory(Long id) {
        if (companyCategoryRepository.existsById(id)) {
            companyCategoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}