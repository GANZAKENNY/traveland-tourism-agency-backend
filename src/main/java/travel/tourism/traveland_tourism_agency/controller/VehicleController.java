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
import org.springframework.web.bind.annotation.RestController;

import travel.tourism.traveland_tourism_agency.domain.Vehicle;
import travel.tourism.traveland_tourism_agency.service.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/save")
    public ResponseEntity<String> saveVehicle(@RequestBody Vehicle vehicle) {
        String result = vehicleService.saveVehicle(vehicle);
        if (result.equals("Vehicle saved successfully")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return new ResponseEntity<>(vehicleService.getAllVehicles(), HttpStatus.OK);
    }

    @GetMapping("/free")
    public ResponseEntity<List<Vehicle>> getFreeVehicles() {
        return new ResponseEntity<>(vehicleService.getFreeVehicles(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVehicleById(@PathVariable UUID id) {
        Optional<Vehicle> vehicle = vehicleService.getVehicleById(id);
        if (vehicle.isPresent()) {
            return new ResponseEntity<>(vehicle.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The vehicle with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateVehicle(@PathVariable UUID id, @RequestBody Vehicle vehicle) {
        String result = vehicleService.updateVehicle(id, vehicle);
        if (result.equals("Vehicle updated successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVehicle(@PathVariable UUID id) {
        String result = vehicleService.deleteVehicle(id);
        if (result.equals("Vehicle deleted successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else if (result.equals("That vehicle is used by a tour guide")) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
}