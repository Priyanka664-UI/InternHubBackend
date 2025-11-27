package SmartInternshipApp.InternHubBackend.config;

import SmartInternshipApp.InternHubBackend.repository.CompanyRepository;
import SmartInternshipApp.InternHubBackend.repository.InternshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private InternshipRepository internshipRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // This will trigger table creation
        companyRepository.count();
        internshipRepository.count();
    }
}