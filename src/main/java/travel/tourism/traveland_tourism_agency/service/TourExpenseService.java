package travel.tourism.traveland_tourism_agency.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import travel.tourism.traveland_tourism_agency.domain.TourBooking;
import travel.tourism.traveland_tourism_agency.domain.TourExpense;
import travel.tourism.traveland_tourism_agency.domain.TourStatus;
import travel.tourism.traveland_tourism_agency.repository.TourBookingRepository;
import travel.tourism.traveland_tourism_agency.repository.TourExpenseRepository;

@Service 
public class TourExpenseService {
   @Autowired
    private TourExpenseRepository tourExpenseRepository;

    @Autowired
    private TourBookingRepository tourBookingRepository;

    public String saveTourExpense(TourExpense tourExpense, UUID tourBookingId) {
        Optional<TourBooking> tourBooking = tourBookingRepository.findById(tourBookingId);
        if (tourBooking.isEmpty()) {
            return "The tour booking with that id does not exist";
        }
        if (tourBooking.get().getStatus() != TourStatus.CONDUCTED) {
            return "Tour expenses are only for CONDUCTED tour bookings";
        }
        tourExpense.setSpentDate(LocalDate.now());
        tourExpense.setTourBooking(tourBooking.get());
        tourExpenseRepository.save(tourExpense);
        return "Tour expense saved successfully";
    }

    public List<TourExpense> getAllTourExpenses() {
        return tourExpenseRepository.findAll();
    }

    public List<TourExpense> getTouristHistory(String passportNumber) {
        return tourExpenseRepository.findTouristHistory(passportNumber);
    }

    public String deleteTourExpense(UUID id) {
        if (!tourExpenseRepository.existsById(id)) {
            return "The tour expense with that id does not exist";
        }
        tourExpenseRepository.deleteById(id);
        return "Tour expense deleted successfully";
    } 
}
