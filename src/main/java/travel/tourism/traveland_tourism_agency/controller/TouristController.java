package travel.tourism.traveland_tourism_agency.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import travel.tourism.traveland_tourism_agency.domain.Tourist;
import travel.tourism.traveland_tourism_agency.service.TouristGuideService;
import travel.tourism.traveland_tourism_agency.service.TouristService;

@RestController
@RequestMapping("/api/tourists")
public class TouristController {

    @Autowired
    private TouristService touristService;

    @Autowired
    private TouristGuideService tourGuideService;

    @PostMapping("/save")
    public ResponseEntity<String> saveTourist(@RequestBody Tourist tourist) {
        String result = touristService.saveTourist(tourist);
        if (result.equals("Tourist saved successfully")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tourist>> getAllTourists() {
        return new ResponseEntity<>(touristService.getAllTourists(), HttpStatus.OK);
    }

    @GetMapping("/by-nationality")
    public ResponseEntity<List<Tourist>> getTouristsByNationality(@RequestParam String nationality) {
        return new ResponseEntity<>(touristService.getTouristsByNationality(nationality), HttpStatus.OK);
    }

    @GetMapping("/of-tour-guide/{tourGuideId}")
    public ResponseEntity<?> getTouristsOfTourGuide(@PathVariable UUID tourGuideId) {
        if (tourGuideService.getTourGuideById(tourGuideId).isEmpty()) {
            return new ResponseEntity<>("The tour guide with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(touristService.getTouristsOfTourGuide(tourGuideId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTouristById(@PathVariable UUID id) {
        Optional<Tourist> tourist = touristService.getTouristById(id);
        if (tourist.isPresent()) {
            return new ResponseEntity<>(tourist.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The tourist with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTourist(@PathVariable UUID id, @RequestBody Tourist tourist) {
        String result = touristService.updateTourist(id, tourist);
        if (result.equals("Tourist updated successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTourist(@PathVariable UUID id) {
        String result = touristService.deleteTourist(id);
        if (result.equals("Tourist deleted successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else if (result.equals("That tourist has tour bookings, delete them first")) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
}
