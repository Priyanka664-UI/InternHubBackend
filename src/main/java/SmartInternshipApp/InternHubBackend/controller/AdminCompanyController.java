package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.Company;
import SmartInternshipApp.InternHubBackend.entity.CompanyCategory;
import SmartInternshipApp.InternHubBackend.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/companies")
public class AdminCompanyController {
    
    @Autowired
    private CompanyService companyService;
    
    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Optional<Company> company = companyService.getCompanyById(id);
        return company.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        return ResponseEntity.ok(companyService.createCompany(company));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        Company updated = companyService.updateCompany(id, company);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        boolean deleted = companyService.deleteCompany(id);
        return deleted ? ResponseEntity.ok("Company deleted successfully") : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Company>> searchCompanies(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(companyService.searchCompanies(keyword));
    }
    
    @GetMapping("/categories")
    public ResponseEntity<CompanyCategory[]> getAllCategories() {
        return ResponseEntity.ok(CompanyCategory.values());
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Company>> getCompaniesByCategory(@PathVariable CompanyCategory category) {
        return ResponseEntity.ok(companyService.getCompaniesByCategory(category));
    }
}