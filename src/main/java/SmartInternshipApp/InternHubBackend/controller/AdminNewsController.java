package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.News;
import SmartInternshipApp.InternHubBackend.entity.Company;
import SmartInternshipApp.InternHubBackend.dto.NewsDTO;
import SmartInternshipApp.InternHubBackend.service.NewsService;
import SmartInternshipApp.InternHubBackend.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/news")
@CrossOrigin(origins = "*")
public class AdminNewsController {
    @Autowired
    private NewsService newsService;
    
    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping
    public ResponseEntity<List<NewsDTO>> getAllNews() {
        return ResponseEntity.ok(newsService.getAllNews());
    }

    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody News news, 
                                          @RequestHeader(value = "X-Company-Id", required = false) String companyId) {
        if (companyId != null && !companyId.equals("0")) {
            Optional<Company> company = companyRepository.findById(Long.parseLong(companyId));
            company.ifPresent(news::setCompany);
        }
        return ResponseEntity.ok(newsService.createNews(news));
    }

    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News news) {
        return ResponseEntity.ok(newsService.updateNews(id, news));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok().build();
    }
}