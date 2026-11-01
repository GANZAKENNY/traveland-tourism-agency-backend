package travel.tourism.traveland_tourism_agency.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import travel.tourism.traveland_tourism_agency.domain.TourExpense;
import travel.tourism.traveland_tourism_agency.service.TourExpenseService;

@RestController
@RequestMapping("/api/tour-expenses")
public class TourExpenseController {

    @Autowired
    private TourExpenseService tourExpenseService;

    @PostMapping("/save")
    public ResponseEntity<String> saveTourExpense(@RequestBody TourExpense tourExpense, @RequestParam UUID tourBookingId) {
        String result = tourExpenseService.saveTourExpense(tourExpense, tourBookingId);
        if (result.equals("Tour expense saved successfully")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else if (result.equals("Tour expenses are only for CONDUCTED tour bookings")) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TourExpense>> getAllTourExpenses() {
        return new ResponseEntity<>(tourExpenseService.getAllTourExpenses(), HttpStatus.OK);
    }

    @GetMapping("/tourist/{passportNumber}")
    public ResponseEntity<List<TourExpense>> getTouristHistory(@PathVariable String passportNumber) {
        return new ResponseEntity<>(tourExpenseService.getTouristHistory(passportNumber), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTourExpense(@PathVariable UUID id) {
        String result = tourExpenseService.deleteTourExpense(id);
        if (result.equals("Tour expense deleted successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
}
