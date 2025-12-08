package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Course;
import SmartInternshipApp.InternHubBackend.entity.CourseCategory;
import SmartInternshipApp.InternHubBackend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
    
    @GetMapping("/{id}/categories")
    public ResponseEntity<Set<CourseCategory>> getCategoriesByCourse(@PathVariable Long id) {
        return courseService.getCourseById(id)
            .map(course -> ResponseEntity.ok(course.getCategories()))
            .orElse(ResponseEntity.notFound().build());
    }
}
