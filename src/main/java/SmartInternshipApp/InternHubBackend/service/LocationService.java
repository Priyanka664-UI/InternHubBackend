package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.State;
import SmartInternshipApp.InternHubBackend.entity.City;
import SmartInternshipApp.InternHubBackend.repository.StateRepository;
import SmartInternshipApp.InternHubBackend.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationService {
    
    @Autowired
    private StateRepository stateRepository;
    
    @Autowired
    private CityRepository cityRepository;
    
    // State operations
    public List<State> getAllStates() {
        return stateRepository.findAll();
    }
    
    public State saveState(State state) {
        return stateRepository.save(state);
    }
    
    public void deleteState(Long id) {
        stateRepository.deleteById(id);
    }
    
    // City operations
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
    
    public List<City> getCitiesByState(Long stateId) {
        return cityRepository.findByStateId(stateId);
    }
    
    public City saveCity(City city) {
        return cityRepository.save(city);
    }
    
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}