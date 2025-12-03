package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.InternshipCategory;
import SmartInternshipApp.InternHubBackend.service.InternshipCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class InternshipCategoryController {
    
    @Autowired
    private InternshipCategoryService service;
    
    @GetMapping
    public ResponseEntity<List<InternshipCategory>> getAllCategories() {
        return ResponseEntity.ok(service.getAllCategories());
    }
}
