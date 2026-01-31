package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Company;
import SmartInternshipApp.InternHubBackend.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LocationValidationService {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Value("${office.allowed.radius.km:0.5}")
    private double allowedRadiusKm;
    
    public boolean isLocationValid(double userLatitude, double userLongitude, Long companyId) {
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company == null || company.getLatitude() == null || company.getLongitude() == null) {
            return false; // Company location not set
        }
        
        double distance = calculateDistance(company.getLatitude(), company.getLongitude(), userLatitude, userLongitude);
        return distance <= allowedRadiusKm;
    }
    
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Distance in km
        
        return distance;
    }
    
    public String getLocationValidationMessage(double userLatitude, double userLongitude, Long companyId) {
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company == null) {
            return "Company not found";
        }
        if (company.getLatitude() == null || company.getLongitude() == null) {
            return "Company location not configured";
        }
        
        if (isLocationValid(userLatitude, userLongitude, companyId)) {
            return "Location verified - within company premises";
        } else {
            double distance = calculateDistance(company.getLatitude(), company.getLongitude(), userLatitude, userLongitude);
            return String.format("Location invalid - %.2f km away from %s", distance, company.getName());
        }
    }
}