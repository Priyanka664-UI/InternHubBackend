package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.CourseCategory;
import SmartInternshipApp.InternHubBackend.service.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/course-categories")
public class AdminCourseCategoryController {
    
    @Autowired
    private CourseCategoryService categoryService;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCategories() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<CourseCategory> categories = categoryService.getAllCategories();
            response.put("success", true);
            response.put("data", categories);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCategoryById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            return categoryService.getCategoryById(id)
                .map(category -> {
                    response.put("success", true);
                    response.put("data", category);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.put("success", false);
                    response.put("message", "Category not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCategory(@RequestBody CourseCategory category) {
        Map<String, Object> response = new HashMap<>();
        try {
            CourseCategory saved = categoryService.createCategory(category);
            response.put("success", true);
            response.put("data", saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCategory(@PathVariable Long id, @RequestBody CourseCategory category) {
        Map<String, Object> response = new HashMap<>();
        try {
            CourseCategory updated = categoryService.updateCategory(id, category);
            if (updated != null) {
                response.put("success", true);
                response.put("data", updated);
                return ResponseEntity.ok(response);
            }
            response.put("success", false);
            response.put("message", "Category not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (categoryService.deleteCategory(id)) {
                response.put("success", true);
                response.put("message", "Category deleted");
                return ResponseEntity.ok(response);
            }
            response.put("success", false);
            response.put("message", "Category not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PutMapping("/{id}/courses")
    public ResponseEntity<Map<String, Object>> updateCategoryCourses(@PathVariable Long id, @RequestBody Set<Long> courseIds) {
        Map<String, Object> response = new HashMap<>();
        try {
            CourseCategory updated = categoryService.updateCategoryCourses(id, courseIds);
            if (updated != null) {
                response.put("success", true);
                response.put("data", updated);
                return ResponseEntity.ok(response);
            }
            response.put("success", false);
            response.put("message", "Category not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
