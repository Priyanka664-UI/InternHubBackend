package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Course;
import SmartInternshipApp.InternHubBackend.entity.CourseCategory;
import SmartInternshipApp.InternHubBackend.repository.CourseCategoryRepository;
import SmartInternshipApp.InternHubBackend.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CourseCategoryService {
    
    @Autowired
    private CourseCategoryRepository categoryRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    public List<CourseCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    public Optional<CourseCategory> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    
    @Transactional
    public CourseCategory createCategory(CourseCategory category) {
        return categoryRepository.save(category);
    }
    
    @Transactional
    public CourseCategory updateCategory(Long id, CourseCategory categoryDetails) {
        Optional<CourseCategory> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            CourseCategory existing = category.get();
            existing.setName(categoryDetails.getName());
            return categoryRepository.save(existing);
        }
        return null;
    }
    
    @Transactional
    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    @Transactional
    public CourseCategory updateCategoryCourses(Long categoryId, Set<Long> courseIds) {
        Optional<CourseCategory> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isPresent()) {
            CourseCategory category = categoryOpt.get();
            Set<Course> courses = new HashSet<>();
            for (Long courseId : courseIds) {
                courseRepository.findById(courseId).ifPresent(courses::add);
            }
            category.setCourses(courses);
            return categoryRepository.save(category);
        }
        return null;
    }
}
