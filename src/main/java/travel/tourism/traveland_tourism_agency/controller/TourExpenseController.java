package travel.tourism.rw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import travel.tourism.rw.domain.TourExpense;
import travel.tourism.rw.service.TourExpenseService;

import java.util.List;
import java.util.UUID;

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
