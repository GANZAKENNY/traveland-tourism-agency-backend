package travel.tourism.traveland_tourism_agency.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import travel.tourism.traveland_tourism_agency.domain.Language;
import travel.tourism.traveland_tourism_agency.domain.TourGuide;
import travel.tourism.traveland_tourism_agency.domain.Vehicle;
import travel.tourism.traveland_tourism_agency.repository.LanguageRepository;
import travel.tourism.traveland_tourism_agency.repository.TourBookingRepository;
import travel.tourism.traveland_tourism_agency.repository.TourGuideRepository;
import travel.tourism.traveland_tourism_agency.repository.VehicleRepository;

@Service 
public class TouristGuideService {

    @Autowired
    private TourGuideRepository tourGuideRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private TourBookingRepository tourBookingRepository;

    // the body can carry "vehicle": {"fleetNumber": 1} and "languages": [{"id": "..."}]
    public String saveTourGuide(TourGuide tourGuide) {
        if (tourGuide.getVehicle() != null) {
            Optional<Vehicle> vehicle = vehicleRepository.findByFleetNumber(tourGuide.getVehicle().getFleetNumber());
            if (vehicle.isEmpty()) {
                return "The vehicle with that fleetNumber does not exist";
            }
            if (tourGuideRepository.existsByVehicle(vehicle.get())) {
                return "That vehicle is taken by another tour guide";
            }
            tourGuide.setVehicle(vehicle.get());
        }

        List<Language> languages = new ArrayList<>();
        for (Language l : tourGuide.getLanguages()) {
            Optional<Language> found = languageRepository.findById(l.getId());
            if (found.isEmpty()) {
                return "That language does not exist";
            }
            languages.add(found.get());
        }
        tourGuide.setLanguages(languages);

        tourGuideRepository.save(tourGuide);
        return "Tour guide saved successfully";
    }

    public List<TourGuide> getAllTourGuides() {
        return tourGuideRepository.findAll();
    }

    public Optional<TourGuide> getTourGuideById(UUID id) {
        return tourGuideRepository.findById(id);
    }

    public List<TourGuide> getTourGuidesByLanguage(String name) {
        return tourGuideRepository.findTourGuidesByLanguage(name);
    }

    @Transactional
    public String addLanguage(UUID tourGuideId, UUID languageId) {
        Optional<TourGuide> tourGuide = tourGuideRepository.findById(tourGuideId);
        if (tourGuide.isEmpty()) {
            return "The tour guide with that id does not exist";
        }
        Optional<Language> language = languageRepository.findById(languageId);
        if (language.isEmpty()) {
            return "That language does not exist";
        }
        TourGuide g = tourGuide.get();
        if (g.getLanguages().contains(language.get())) {
            return "The tour guide already has that language";
        }
        // TourGuide owns the join table, so we change its list and save it
        g.getLanguages().add(language.get());
        tourGuideRepository.save(g);
        return "Language added to the tour guide";
    }

    public String changeVehicle(UUID tourGuideId, int fleetNumber) {
        Optional<TourGuide> tourGuide = tourGuideRepository.findById(tourGuideId);
        if (tourGuide.isEmpty()) {
            return "The tour guide with that id does not exist";
        }
        Optional<Vehicle> vehicle = vehicleRepository.findByFleetNumber(fleetNumber);
        if (vehicle.isEmpty()) {
            return "The vehicle with that fleetNumber does not exist";
        }
        if (tourGuideRepository.existsByVehicleAndIdNot(vehicle.get(), tourGuideId)) {
            return "That vehicle is taken by another tour guide";
        }
        // the old vehicle is no longer referenced, so it becomes free
        TourGuide g = tourGuide.get();
        g.setVehicle(vehicle.get());
        tourGuideRepository.save(g);
        return "Vehicle changed successfully";
    }

    public String updateTourGuide(UUID id, TourGuide tourGuide) {
        Optional<TourGuide> existing = tourGuideRepository.findById(id);
        if (existing.isEmpty()) {
            return "The tour guide with that id does not exist";
        }
        // vehicle and languages have their own endpoints
        TourGuide g = existing.get();
        g.setFirstName(tourGuide.getFirstName());
        g.setLastName(tourGuide.getLastName());
        g.setPhone(tourGuide.getPhone());
        tourGuideRepository.save(g);
        return "Tour guide updated successfully";
    }

    public String deleteTourGuide(UUID id) {
        Optional<TourGuide> tourGuide = tourGuideRepository.findById(id);
        if (tourGuide.isEmpty()) {
            return "The tour guide with that id does not exist";
        }
        if (tourBookingRepository.existsByTourGuide(tourGuide.get())) {
            return "That tour guide has tour bookings, delete them first";
        }
        tourGuideRepository.deleteById(id);
        return "Tour guide deleted successfully";
    }
}