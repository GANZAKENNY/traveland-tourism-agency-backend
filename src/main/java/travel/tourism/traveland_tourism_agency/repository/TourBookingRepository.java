package travel.tourism.traveland_tourism_agency.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import travel.tourism.traveland_tourism_agency.domain.TourBooking;
import travel.tourism.traveland_tourism_agency.domain.TourGuide;
import travel.tourism.traveland_tourism_agency.domain.TourStatus;
import travel.tourism.traveland_tourism_agency.domain.Tourist;

@Repository 
public interface TourBookingRepository extends JpaRepository<TourBooking, UUID> {

    boolean existsByTourist(Tourist tourist);

    boolean existsByTourGuide(TourGuide tourGuide);

    
    List<TourBooking> findByStatusOrderByTourDateAsc(TourStatus status);

    
    List<TourBooking> findByTourDateBetweenOrderByTourDateAsc(LocalDate start, LocalDate end);

    
    boolean existsByTourGuideAndTourDateAndStatusNot(TourGuide tourGuide, LocalDate tourDate, TourStatus status);

    
    long countByStatus(TourStatus status);

    long countByTourDate(LocalDate tourDate);

    
    @Query("SELECT b.status, COUNT(b) FROM TourBooking b GROUP BY b.status")
    List<Object[]> countTourBookingsByStatus();

    @Modifying
    @Transactional
    @Query("UPDATE TourBooking b SET b.status = :cancelledStatus WHERE b.tourGuide.id = :tourGuideId AND b.tourDate = :date AND b.status <> :conductedStatus AND b.status <> :cancelledStatus")
    int cancelTourGuideDay(@Param("tourGuideId") UUID tourGuideId,
                    @Param("date") LocalDate date,
                    @Param("cancelledStatus") TourStatus cancelledStatus,
                    @Param("conductedStatus") TourStatus conductedStatus);
}
    

