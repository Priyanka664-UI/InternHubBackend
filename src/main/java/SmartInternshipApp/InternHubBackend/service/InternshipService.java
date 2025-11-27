package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Internship;
import SmartInternshipApp.InternHubBackend.repository.InternshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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
    
    public Optional<Internship> getInternshipById(Long id) {
        return internshipRepository.findById(id);
    }
    
    public Internship updateInternship(Long id, Internship internshipDetails) {
        Optional<Internship> optionalInternship = internshipRepository.findById(id);
        if (optionalInternship.isPresent()) {
            Internship internship = optionalInternship.get();
            internship.setTitle(internshipDetails.getTitle());
            internship.setCompany(internshipDetails.getCompany());
            internship.setDescription(internshipDetails.getDescription());
            internship.setLocation(internshipDetails.getLocation());
            internship.setDuration(internshipDetails.getDuration());
            internship.setStipend(internshipDetails.getStipend());
            internship.setSkillsRequired(internshipDetails.getSkillsRequired());
            internship.setStateId(internshipDetails.getStateId());
            internship.setCityId(internshipDetails.getCityId());
            internship.setWorkType(internshipDetails.getWorkType());
            return internshipRepository.save(internship);
        }
        return null;
    }
    
    public boolean deleteInternship(Long id) {
        if (internshipRepository.existsById(id)) {
            internshipRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Location-based search methods
    public List<Internship> getInternshipsByState(Long stateId) {
        return internshipRepository.findByStateId(stateId);
    }
    
    public List<Internship> getInternshipsByCity(Long cityId) {
        return internshipRepository.findByCityId(cityId);
    }
    
    public List<Internship> getInternshipsByWorkType(String workType) {
        return internshipRepository.findByWorkType(workType);
    }
    
    public List<Internship> getInternshipsByLocation(Long stateId, Long cityId) {
        if (stateId != null && cityId != null) {
            return internshipRepository.findByStateIdAndCityId(stateId, cityId);
        } else if (stateId != null) {
            return internshipRepository.findByStateId(stateId);
        } else if (cityId != null) {
            return internshipRepository.findByCityId(cityId);
        }
        return getAllInternships();
    }
}