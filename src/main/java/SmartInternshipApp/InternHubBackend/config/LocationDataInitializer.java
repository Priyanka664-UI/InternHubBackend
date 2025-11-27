package SmartInternshipApp.InternHubBackend.config;

import SmartInternshipApp.InternHubBackend.entity.State;
import SmartInternshipApp.InternHubBackend.entity.City;
import SmartInternshipApp.InternHubBackend.repository.StateRepository;
import SmartInternshipApp.InternHubBackend.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LocationDataInitializer implements CommandLineRunner {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public void run(String... args) throws Exception {
        if (stateRepository.count() == 0) {
            initializeStatesAndCities();
        }
    }

    private void initializeStatesAndCities() {
        // Create states
        State maharashtra = new State();
        maharashtra.setName("Maharashtra");
        maharashtra.setCode("MH");
        maharashtra = stateRepository.save(maharashtra);

        State delhi = new State();
        delhi.setName("Delhi");
        delhi.setCode("DL");
        delhi = stateRepository.save(delhi);

        State karnataka = new State();
        karnataka.setName("Karnataka");
        karnataka.setCode("KA");
        karnataka = stateRepository.save(karnataka);

        // Create cities for Maharashtra
        City mumbai = new City();
        mumbai.setName("Mumbai");
        mumbai.setStateId(maharashtra.getId());
        cityRepository.save(mumbai);

        City pune = new City();
        pune.setName("Pune");
        pune.setStateId(maharashtra.getId());
        cityRepository.save(pune);

        // Create cities for Delhi
        City newDelhi = new City();
        newDelhi.setName("New Delhi");
        newDelhi.setStateId(delhi.getId());
        cityRepository.save(newDelhi);

        // Create cities for Karnataka
        City bangalore = new City();
        bangalore.setName("Bangalore");
        bangalore.setStateId(karnataka.getId());
        cityRepository.save(bangalore);

        System.out.println("Initial location data created successfully!");
    }
}