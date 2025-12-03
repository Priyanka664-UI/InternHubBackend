package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Company;

import SmartInternshipApp.InternHubBackend.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/admin/companies")
public class AdminCompanyController {
    
    @Autowired
    private CompanyService companyService;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCompanies() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Company> companies = companyService.getAllCompanies();
            response.put("success", true);
            response.put("data", companies);
            response.put("message", "Companies retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("data", new ArrayList<>());
            response.put("message", "Error retrieving companies: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCompanyById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Company> company = companyService.getCompanyById(id);
            if (company.isPresent()) {
                response.put("success", true);
                response.put("data", company.get());
                response.put("message", "Company found");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Company not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving company: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCompany(@RequestBody Company company) {
        Map<String, Object> response = new HashMap<>();
        try {
            Company savedCompany = companyService.createCompany(company);
            response.put("success", true);
            response.put("data", savedCompany);
            response.put("message", "Company created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating company: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        Map<String, Object> response = new HashMap<>();
        try {
            Company updated = companyService.updateCompany(id, company);
            if (updated != null) {
                response.put("success", true);
                response.put("data", updated);
                response.put("message", "Company updated successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Company not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating company: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCompany(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = companyService.deleteCompany(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "Company deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Company not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting company: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchCompanies(@RequestParam(required = false) String keyword) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Company> companies = companyService.searchCompanies(keyword);
            response.put("success", true);
            response.put("data", companies);
            response.put("message", "Search completed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("data", new ArrayList<>());
            response.put("message", "Error searching companies: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    

    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Map<String, Object>> getCompaniesByCategory(@PathVariable Long categoryId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Company> companies = companyService.getCompaniesByCategoryId(categoryId);
            response.put("success", true);
            response.put("data", companies);
            response.put("message", "Companies retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("data", new ArrayList<>());
            response.put("message", "Error retrieving companies: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}