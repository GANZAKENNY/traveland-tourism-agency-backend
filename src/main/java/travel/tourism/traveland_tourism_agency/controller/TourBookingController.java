package travel.tourism.traveland_tourism_agency.controller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tour-bookings")
public class TourBookingController {

    @Autowired
    private TourBookingService tourBookingService;

    @PostMapping("/save")
    public ResponseEntity<String> saveTourBooking(@RequestBody TourBooking tourBooking) {
        String result = tourBookingService.saveTourBooking(tourBooking);
        if (result.equals("Tour booking saved successfully")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else if (result.equals("Tour guide is already booked on that date")) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        } else if (result.equals("Tour booking date cannot be in the past")) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TourBooking>> getAllTourBookings() {
        return new ResponseEntity<>(tourBookingService.getAllTourBookings(), HttpStatus.OK);
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<TourBooking>> getTourBookingsByStatus(@RequestParam TourStatus status) {
        return new ResponseEntity<>(tourBookingService.getTourBookingsByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/between")
    public ResponseEntity<List<TourBooking>> getTourBookingsBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return new ResponseEntity<>(tourBookingService.getTourBookingsBetween(startDate, endDate), HttpStatus.OK);
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> countByStatus() {
        return new ResponseEntity<>(tourBookingService.countByStatus(), HttpStatus.OK);
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelTourGuideDay(@RequestParam UUID tourGuideId, @RequestParam String date) {
        String result = tourBookingService.cancelTourGuideDay(tourGuideId, LocalDate.parse(date));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

  
    @GetMapping("/page")
    public ResponseEntity<Page<TourBooking>> getTourBookingsPage(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "5") int size,
                                                                 @RequestParam(defaultValue = "tourDate,asc") String sort) {
        String[] parts = sort.split(",");
        Sort.Direction direction = Sort.Direction.ASC;
        if (parts.length > 1 && parts[1].equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, parts[0]));
        return new ResponseEntity<>(tourBookingService.getTourBookingsPage(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTourBookingById(@PathVariable UUID id) {
        Optional<TourBooking> tourBooking = tourBookingService.getTourBookingById(id);
        if (tourBooking.isPresent()) {
            return new ResponseEntity<>(tourBooking.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The tour booking with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTourBooking(@PathVariable UUID id, @RequestBody TourBooking tourBooking) {
        String result = tourBookingService.updateTourBooking(id, tourBooking);
        if (result.equals("Tour booking updated successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(@PathVariable UUID id, @RequestParam TourStatus status) {
        String result = tourBookingService.changeStatus(id, status);
        if (result.equals("Tour booking status updated successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else if (result.startsWith("Cannot change status")) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTourBooking(@PathVariable UUID id) {
        String result = tourBookingService.deleteTourBooking(id);
        if (result.equals("Tour booking deleted successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else if (result.equals("That tour booking has tour expenses, delete them first")) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
}
