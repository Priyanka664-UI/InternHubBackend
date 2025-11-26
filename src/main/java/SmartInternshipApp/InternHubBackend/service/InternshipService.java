package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Internship;
import SmartInternshipApp.InternHubBackend.repository.InternshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InternshipService {
    
    @Autowired
    private InternshipRepository internshipRepository;
    
    public List<Internship> getAllInternships() {
        return internshipRepository.findAll();
    }
    
    public List<Internship> searchInternships(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllInternships();
        }
        return internshipRepository.searchInternships(keyword);
    }
    
    public Internship createInternship(Internship internship) {
        return internshipRepository.save(internship);
    }
}