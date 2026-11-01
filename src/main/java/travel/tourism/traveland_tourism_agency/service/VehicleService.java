package travel.tourism.traveland_tourism_agency.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import travel.tourism.traveland_tourism_agency.domain.Vehicle;
import travel.tourism.traveland_tourism_agency.repository.TourGuideRepository;
import travel.tourism.traveland_tourism_agency.repository.VehicleRepository;

@Service 
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private TourGuideRepository tourGuideRepository;

    public String saveVehicle(Vehicle vehicle) {
        if (vehicleRepository.existsByFleetNumber(vehicle.getFleetNumber())) {
            return "A vehicle with that fleetNumber already exists";
        }
        vehicleRepository.save(vehicle);
        return "Vehicle saved successfully";
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleById(UUID id) {
        return vehicleRepository.findById(id);
    }

    public List<Vehicle> getFreeVehicles() {
        return vehicleRepository.findFreeVehicles();
    }

    public String updateVehicle(UUID id, Vehicle vehicle) {
        Optional<Vehicle> existing = vehicleRepository.findById(id);
        if (existing.isEmpty()) {
            return "The vehicle with that id does not exist";
        }
        // the fleetNumber is the business key, only the name changes
        Vehicle v = existing.get();
        v.setName(vehicle.getName());
        vehicleRepository.save(v);
        return "Vehicle updated successfully";
    }

    public String deleteVehicle(UUID id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        if (vehicle.isEmpty()) {
            return "The vehicle with that id does not exist";
        }
        if (tourGuideRepository.existsByVehicle(vehicle.get())) {
            return "That vehicle is used by a tour guide";
        }
        vehicleRepository.deleteById(id);
        return "Vehicle deleted successfully";
    }
}
