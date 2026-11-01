package travel.tourism.traveland_tourism_agency.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import travel.tourism.traveland_tourism_agency.domain.Tourist;
import travel.tourism.traveland_tourism_agency.repository.TourBookingRepository;
import travel.tourism.traveland_tourism_agency.repository.TouristRepository;

@Service 
public class TouristService {
    @Autowired
    private TouristRepository touristRepository;

    @Autowired
    private TourBookingRepository tourBookingRepository;

    public String saveTourist(Tourist tourist) {
        if (touristRepository.existsByPassportNumber(tourist.getPassportNumber())) {
            return "A tourist with that passportNumber already exists";
        }
        touristRepository.save(tourist);
        return "Tourist saved successfully";
    }

    public List<Tourist> getAllTourists() {
        return touristRepository.findAll();
    }

    public Optional<Tourist> getTouristById(UUID id) {
        return touristRepository.findById(id);
    }

    public List<Tourist> getTouristsByNationality(String nationality) {
        return touristRepository.findByNationalityIgnoreCaseOrderByLastNameAsc(nationality);
    }

    public List<Tourist> getTouristsOfTourGuide(UUID tourGuideId) {
        return touristRepository.findTouristsOfTourGuide(tourGuideId);
    }

    public String updateTourist(UUID id, Tourist tourist) {
        Optional<Tourist> existing = touristRepository.findById(id);
        if (existing.isEmpty()) {
            return "The tourist with that id does not exist";
        }
        // passportNumber is the business key, it does not change
        Tourist t = existing.get();
        t.setFirstName(tourist.getFirstName());
        t.setLastName(tourist.getLastName());
        t.setNationality(tourist.getNationality());
        touristRepository.save(t);
        return "Tourist updated successfully";
    }

    public String deleteTourist(UUID id) {
        Optional<Tourist> tourist = touristRepository.findById(id);
        if (tourist.isEmpty()) {
            return "The tourist with that id does not exist";
        }
        if (tourBookingRepository.existsByTourist(tourist.get())) {
            return "That tourist has tour bookings, delete them first";
        }
        touristRepository.deleteById(id);
        return "Tourist deleted successfully";
    }
}
