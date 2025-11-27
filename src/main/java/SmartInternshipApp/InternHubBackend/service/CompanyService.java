package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Company;
import SmartInternshipApp.InternHubBackend.entity.CompanyCategory;
import SmartInternshipApp.InternHubBackend.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
    
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }
    
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }
    
    public Company updateCompany(Long id, Company companyDetails) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            company.setName(companyDetails.getName());
            company.setEmail(companyDetails.getEmail());
            company.setWebsite(companyDetails.getWebsite());
            company.setStateId(companyDetails.getStateId());
            company.setCityId(companyDetails.getCityId());
            company.setIndustry(companyDetails.getIndustry());
            company.setDescription(companyDetails.getDescription());
            company.setContactPerson(companyDetails.getContactPerson());
            company.setContactPhone(companyDetails.getContactPhone());
            company.setCategory(companyDetails.getCategory());
            return companyRepository.save(company);
        }
        return null;
    }
    
    public boolean deleteCompany(Long id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<Company> searchCompanies(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCompanies();
        }
        return companyRepository.findByKeyword(keyword.trim());
    }
    
    public List<Company> getCompaniesByCategory(CompanyCategory category) {
        return companyRepository.findByCategory(category);
    }
}