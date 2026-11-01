package travel.tourism.traveland_tourism_agency.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import travel.tourism.traveland_tourism_agency.domain.Tourist;

@Repository 
public interface TouristRepository  extends JpaRepository<Tourist, UUID> {

    Optional<Tourist> findByPassportNumber(String passportNumber);
    
    boolean existsByPassportNumber(String passportNumber);

    List<Tourist> findByNationalityIgnoreCaseOrderByLastNameAsc(String nationality);

    @Query("SELECT DISTINCT b.tourist FROM TourBooking b WHERE b.tourGuide.id = :tourGuideId")
    List<Tourist> findTouristsOfTourGuide(@Param("tourGuideId") UUID tourGuideId);
    
}
