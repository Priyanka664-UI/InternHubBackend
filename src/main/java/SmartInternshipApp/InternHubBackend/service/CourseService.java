package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Course;
import SmartInternshipApp.InternHubBackend.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }
    
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }
    
    public Course updateCourse(Long id, Course updatedCourse) {
        Optional<Course> existing = courseRepository.findById(id);
        if (existing.isPresent()) {
            Course course = existing.get();
            if (updatedCourse.getName() != null) course.setName(updatedCourse.getName());
            if (updatedCourse.getDescription() != null) course.setDescription(updatedCourse.getDescription());
            if (updatedCourse.getDurationMonths() != null) course.setDurationMonths(updatedCourse.getDurationMonths());
            return courseRepository.save(course);
        }
        return null;
    }
    
    public boolean deleteCourse(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<Course> searchCourses(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCourses();
        }
        return courseRepository.findByKeyword(keyword.trim());
    }
}
