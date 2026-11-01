package travel.tourism.traveland_tourism_agency.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import travel.tourism.traveland_tourism_agency.domain.Vehicle;

@Repository 
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    Optional<Vehicle> findByFleetNumber(int fleetNumber);
    
    boolean existsByFleetNumber(int fleetNumber);

    @Query("SELECT v FROM Vehicle v WHERE NOT EXISTS (SELECT g FROM TourGuide g WHERE g.vehicle = v) ORDER BY v.fleetNumber")
    List<Vehicle> findFreeVehicles();
    
}
