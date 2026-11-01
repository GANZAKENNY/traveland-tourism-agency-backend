package travel.tourism.traveland_tourism_agency.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import travel.tourism.traveland_tourism_agency.domain.TourGuide;
import travel.tourism.traveland_tourism_agency.domain.Vehicle;

@Repository 
public interface TourGuideRepository extends JpaRepository<TourGuide, UUID> {

    boolean existsByVehicle(Vehicle vehicle);

    
    boolean existsByVehicleAndIdNot(Vehicle vehicle, UUID id);

    
    @Query("SELECT g FROM TourGuide g JOIN g.languages l WHERE LOWER(l.name) = LOWER(:name)")
    List<TourGuide> findTourGuidesByLanguage(@Param("name") String name);
    
}
