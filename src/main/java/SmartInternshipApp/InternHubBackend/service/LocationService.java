package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.State;
import SmartInternshipApp.InternHubBackend.entity.City;
import SmartInternshipApp.InternHubBackend.dto.CityDTO;
import SmartInternshipApp.InternHubBackend.repository.StateRepository;
import SmartInternshipApp.InternHubBackend.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.function.Function;

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
    
    public List<CityDTO> getAllCitiesWithStateNames() {
        List<City> cities = cityRepository.findAll();
        List<State> states = stateRepository.findAll();
        
        System.out.println("Found " + cities.size() + " cities and " + states.size() + " states");
        
        Map<Long, String> stateMap = states.stream()
            .collect(Collectors.toMap(State::getId, State::getName));
        
        return cities.stream()
            .map(city -> {
                String stateName = stateMap.get(city.getStateId());
                System.out.println("City: " + city.getName() + ", StateId: " + city.getStateId() + ", StateName: " + stateName);
                return new CityDTO(
                    city.getId(),
                    city.getName(),
                    city.getStateId(),
                    stateName != null ? stateName : "Unknown State"
                );
            })
            .collect(Collectors.toList());
    }
    
    public List<City> getCitiesByState(Long stateId) {
        return cityRepository.findByStateId(stateId);
    }
    
    public City saveCity(City city) {
        System.out.println("Saving city: " + city.getName() + " with ID: " + city.getId() + " and stateId: " + city.getStateId());
        City savedCity = cityRepository.save(city);
        System.out.println("Saved city with ID: " + savedCity.getId());
        return savedCity;
    }
    
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}