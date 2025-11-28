package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Company;
import SmartInternshipApp.InternHubBackend.entity.CompanyCategory;
import SmartInternshipApp.InternHubBackend.repository.CompanyRepository;
import SmartInternshipApp.InternHubBackend.repository.StateRepository;
import SmartInternshipApp.InternHubBackend.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private StateRepository stateRepository;
    
    @Autowired
    private CityRepository cityRepository;
    
    public List<Company> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        populateLocationNames(companies);
        return companies;
    }
    
    private void populateLocationNames(List<Company> companies) {
        for (Company company : companies) {
            if (company.getStateId() != null) {
                stateRepository.findById(company.getStateId())
                    .ifPresent(state -> company.setStateName(state.getName()));
            }
            if (company.getCityId() != null) {
                cityRepository.findById(company.getCityId())
                    .ifPresent(city -> company.setCityName(city.getName()));
            }
        }
    }
    
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }
    
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }
    
    public Company saveCompany(Company company) {
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