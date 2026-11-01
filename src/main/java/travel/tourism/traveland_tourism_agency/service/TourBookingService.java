package travel.tourism.traveland_tourism_agency.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import travel.tourism.traveland_tourism_agency.domain.TourBooking;
import travel.tourism.traveland_tourism_agency.domain.TourGuide;
import travel.tourism.traveland_tourism_agency.domain.TourStatus;
import travel.tourism.traveland_tourism_agency.domain.Tourist;
import travel.tourism.traveland_tourism_agency.repository.TourBookingRepository;
import travel.tourism.traveland_tourism_agency.repository.TourExpenseRepository;
import travel.tourism.traveland_tourism_agency.repository.TourGuideRepository;
import travel.tourism.traveland_tourism_agency.repository.TouristRepository;

@Service 
public class TourBookingService {
    @Autowired
    private TourBookingRepository tourBookingRepository;

    @Autowired
    private TouristRepository touristRepository;

    @Autowired
    private TourGuideRepository tourGuideRepository;

    @Autowired
    private TourExpenseRepository tourExpenseRepository;

    // the body carries "tourist": {"passportNumber": "PC123456"} and "tourGuide": {"id": "..."}
    public String saveTourBooking(TourBooking tourBooking) {
        if (tourBooking.getTourist() == null) {
            return "The tourist with that passportNumber does not exist";
        }
        Optional<Tourist> tourist = touristRepository.findByPassportNumber(tourBooking.getTourist().getPassportNumber());
        if (tourist.isEmpty()) {
            return "The tourist with that passportNumber does not exist";
        }
        if (tourBooking.getTourGuide() == null || tourBooking.getTourGuide().getId() == null) {
            return "The tour guide with that id does not exist";
        }
        Optional<TourGuide> tourGuide = tourGuideRepository.findById(tourBooking.getTourGuide().getId());
        if (tourGuide.isEmpty()) {
            return "The tour guide with that id does not exist";
        }
        if (tourBooking.getTourDate().isBefore(LocalDate.now())) {
            return "Tour booking date cannot be in the past";
        }
        if (tourBookingRepository.existsByTourGuideAndTourDateAndStatusNot(
                tourGuide.get(), tourBooking.getTourDate(), TourStatus.CANCELLED)) {
            return "Tour guide is already booked on that date";
        }
        // a new tour booking always starts as BOOKED, whatever the body says
        tourBooking.setStatus(TourStatus.BOOKED);
        tourBooking.setTourist(tourist.get());
        tourBooking.setTourGuide(tourGuide.get());
        tourBookingRepository.save(tourBooking);
        return "Tour booking saved successfully";
    }

    public List<TourBooking> getAllTourBookings() {
        return tourBookingRepository.findAll();
    }

    public Optional<TourBooking> getTourBookingById(UUID id) {
        return tourBookingRepository.findById(id);
    }

    public List<TourBooking> getTourBookingsByStatus(TourStatus status) {
        return tourBookingRepository.findByStatusOrderByTourDateAsc(status);
    }

    public List<TourBooking> getTourBookingsBetween(LocalDate start, LocalDate end) {
        return tourBookingRepository.findByTourDateBetweenOrderByTourDateAsc(start, end);
    }

    public List<Object[]> countByStatus() {
        return tourBookingRepository.countTourBookingsByStatus();
    }

    @Transactional
    public String cancelTourGuideDay(UUID tourGuideId, LocalDate date) {
        int count = tourBookingRepository.cancelTourGuideDay(tourGuideId, date,TourStatus.CANCELLED, TourStatus.CONDUCTED);
        return count + " tour bookings cancelled";
    }

    public Page<TourBooking> getTourBookingsPage(Pageable pageable) {
        return tourBookingRepository.findAll(pageable);
    }

    public String updateTourBooking(UUID id, TourBooking tourBooking) {
        Optional<TourBooking> existing = tourBookingRepository.findById(id);
        if (existing.isEmpty()) {
            return "The tour booking with that id does not exist";
        }
        // the status only changes through PATCH /{id}/status
        TourBooking b = existing.get();
        b.setTourDate(tourBooking.getTourDate());
        b.setDestination(tourBooking.getDestination());
        b.setTravellers(tourBooking.getTravellers());
        tourBookingRepository.save(b);
        return "Tour booking updated successfully";
    }

    public String changeStatus(UUID id, TourStatus status) {
        Optional<TourBooking> tourBooking = tourBookingRepository.findById(id);
        if (tourBooking.isEmpty()) {
            return "The tour booking with that id does not exist";
        }
        TourBooking b = tourBooking.get();
        if (!canMove(b.getStatus(), status)) {
            return "Cannot change status from " + b.getStatus() + " to " + status;
        }
        b.setStatus(status);
        tourBookingRepository.save(b);
        return "Tour booking status updated successfully";
    }

    private boolean canMove(TourStatus from, TourStatus to) {
        if (from == TourStatus.BOOKED) {
            return to == TourStatus.PAID || to == TourStatus.CANCELLED;
        }
        if (from == TourStatus.PAID) {
            return to == TourStatus.CONDUCTED || to == TourStatus.CANCELLED;
        }
        // CONDUCTED and CANCELLED are final
        return false;
    }

    public String deleteTourBooking(UUID id) {
        Optional<TourBooking> tourBooking = tourBookingRepository.findById(id);
        if (tourBooking.isEmpty()) {
            return "The tour booking with that id does not exist";
        }
        if (tourExpenseRepository.existsByTourBooking(tourBooking.get())) {
            return "That tour booking has tour expenses, delete them first";
        }
        tourBookingRepository.deleteById(id);
        return "Tour booking deleted successfully";
    }
}
