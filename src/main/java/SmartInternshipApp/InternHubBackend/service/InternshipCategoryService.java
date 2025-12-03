package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.InternshipCategory;
import SmartInternshipApp.InternHubBackend.repository.InternshipCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InternshipCategoryService {
    
    @Autowired
    private InternshipCategoryRepository repository;
    
    public List<InternshipCategory> getAllCategories() {
        return repository.findAll();
    }
}
