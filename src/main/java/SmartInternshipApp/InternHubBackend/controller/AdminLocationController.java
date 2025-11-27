package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.State;
import SmartInternshipApp.InternHubBackend.entity.City;
import SmartInternshipApp.InternHubBackend.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/locations")
@CrossOrigin(origins = "*")
public class AdminLocationController {
    
    @Autowired
    private LocationService locationService;
    
    // State endpoints
    @GetMapping("/states")
    public ResponseEntity<List<State>> getAllStates() {
        return ResponseEntity.ok(locationService.getAllStates());
    }
    
    @PostMapping("/states")
    public ResponseEntity<State> createState(@RequestBody State state) {
        return ResponseEntity.ok(locationService.saveState(state));
    }
    
    @PutMapping("/states/{id}")
    public ResponseEntity<State> updateState(@PathVariable Long id, @RequestBody State state) {
        state.setId(id);
        return ResponseEntity.ok(locationService.saveState(state));
    }
    
    @DeleteMapping("/states/{id}")
    public ResponseEntity<String> deleteState(@PathVariable Long id) {
        locationService.deleteState(id);
        return ResponseEntity.ok("State deleted successfully");
    }
    
    // City endpoints
    @GetMapping("/cities")
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(locationService.getAllCities());
    }
    
    @GetMapping("/cities/by-state/{stateId}")
    public ResponseEntity<List<City>> getCitiesByState(@PathVariable Long stateId) {
        return ResponseEntity.ok(locationService.getCitiesByState(stateId));
    }
    
    @PostMapping("/cities")
    public ResponseEntity<City> createCity(@RequestBody City city) {
        return ResponseEntity.ok(locationService.saveCity(city));
    }
    
    @PutMapping("/cities/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @RequestBody City city) {
        city.setId(id);
        return ResponseEntity.ok(locationService.saveCity(city));
    }
    
    @DeleteMapping("/cities/{id}")
    public ResponseEntity<String> deleteCity(@PathVariable Long id) {
        locationService.deleteCity(id);
        return ResponseEntity.ok("City deleted successfully");
    }
}