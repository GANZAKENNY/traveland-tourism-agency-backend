package travel.tourism.rw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import travel.tourism.rw.domain.TourGuide;
import travel.tourism.rw.service.TourGuideService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tour-guides")
public class TourGuideController {

    @Autowired
    private TourGuideService tourGuideService;

    @PostMapping("/save")
    public ResponseEntity<String> saveTourGuide(@RequestBody TourGuide tourGuide) {
        String result = tourGuideService.saveTourGuide(tourGuide);
        if (result.equals("Tour guide saved successfully")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else if (result.equals("That vehicle is taken by another tour guide")) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TourGuide>> getAllTourGuides() {
        return new ResponseEntity<>(tourGuideService.getAllTourGuides(), HttpStatus.OK);
    }

    @GetMapping("/by-language")
    public ResponseEntity<List<TourGuide>> getTourGuidesByLanguage(@RequestParam String name) {
        return new ResponseEntity<>(tourGuideService.getTourGuidesByLanguage(name), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTourGuideById(@PathVariable UUID id) {
        Optional<TourGuide> tourGuide = tourGuideService.getTourGuideById(id);
        if (tourGuide.isPresent()) {
            return new ResponseEntity<>(tourGuide.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The tour guide with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{tourGuideId}/languages/{languageId}")
    public ResponseEntity<String> addLanguage(@PathVariable UUID tourGuideId, @PathVariable UUID languageId) {
        String result = tourGuideService.addLanguage(tourGuideId, languageId);
        if (result.equals("Language added to the tour guide")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else if (result.equals("The tour guide already has that language")) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{tourGuideId}/vehicle")
    public ResponseEntity<String> changeVehicle(@PathVariable UUID tourGuideId, @RequestParam int fleetNumber) {
        String result = tourGuideService.changeVehicle(tourGuideId, fleetNumber);
        if (result.equals("Vehicle changed successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else if (result.equals("That vehicle is taken by another tour guide")) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTourGuide(@PathVariable UUID id, @RequestBody TourGuide tourGuide) {
        String result = tourGuideService.updateTourGuide(id, tourGuide);
        if (result.equals("Tour guide updated successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTourGuide(@PathVariable UUID id) {
        String result = tourGuideService.deleteTourGuide(id);
        if (result.equals("Tour guide deleted successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else if (result.equals("That tour guide has tour bookings, delete them first")) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
}
