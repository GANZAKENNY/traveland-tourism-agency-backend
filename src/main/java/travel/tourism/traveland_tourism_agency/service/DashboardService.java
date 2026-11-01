package travel.tourism.traveland_tourism_agency.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import travel.tourism.traveland_tourism_agency.domain.TourStatus;
import travel.tourism.traveland_tourism_agency.repository.TourBookingRepository;
import travel.tourism.traveland_tourism_agency.repository.TourExpenseRepository;
import travel.tourism.traveland_tourism_agency.repository.TourGuideRepository;
import travel.tourism.traveland_tourism_agency.repository.TouristRepository;
import travel.tourism.traveland_tourism_agency.repository.VehicleRepository;

@Service 
public class DashboardService {
    @Autowired
    private TouristRepository touristRepository;

    @Autowired
    private TourGuideRepository tourGuideRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private TourBookingRepository tourBookingRepository;

    @Autowired
    private TourExpenseRepository tourExpenseRepository;

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("tourists", touristRepository.count());
        summary.put("tourGuides", tourGuideRepository.count());
        summary.put("freeVehicles", (long) vehicleRepository.findFreeVehicles().size());
        summary.put("toursToday", tourBookingRepository.countByTourDate(LocalDate.now()));
        summary.put("booked", tourBookingRepository.countByStatus(TourStatus.BOOKED));
        summary.put("tourExpenses", tourExpenseRepository.count());
        return summary;
    }
}
