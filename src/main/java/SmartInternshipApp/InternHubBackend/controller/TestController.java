package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.State;
import SmartInternshipApp.InternHubBackend.entity.City;
import SmartInternshipApp.InternHubBackend.repository.StateRepository;
import SmartInternshipApp.InternHubBackend.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {
    
    @Autowired
    private StateRepository stateRepository;
    
    @Autowired
    private CityRepository cityRepository;
    
    @GetMapping("/db-status")
    public ResponseEntity<String> testDatabase() {
        try {
            long stateCount = stateRepository.count();
            long cityCount = cityRepository.count();
            return ResponseEntity.ok("Database connected! States: " + stateCount + ", Cities: " + cityCount);
        } catch (Exception e) {
            return ResponseEntity.ok("Database error: " + e.getMessage());
        }
    }
    
    @PostMapping("/create-sample-data")
    public ResponseEntity<String> createSampleData() {
        try {
            // Create a test state
            State testState = new State();
            testState.setName("Test State");
            testState.setCode("TS");
            testState = stateRepository.save(testState);
            
            // Create a test city
            City testCity = new City();
            testCity.setName("Test City");
            testCity.setStateId(testState.getId());
            cityRepository.save(testCity);
            
            return ResponseEntity.ok("Sample data created successfully!");
        } catch (Exception e) {
            return ResponseEntity.ok("Error creating data: " + e.getMessage());
        }
    }
}