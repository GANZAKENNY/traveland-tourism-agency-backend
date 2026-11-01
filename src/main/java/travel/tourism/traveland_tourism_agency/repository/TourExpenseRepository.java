package travel.tourism.traveland_tourism_agency.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import travel.tourism.traveland_tourism_agency.domain.TourBooking;
import travel.tourism.traveland_tourism_agency.domain.TourExpense;

@Repository 
public interface TourExpenseRepository extends JpaRepository<TourExpense,UUID>{

    @Query("SELECT d FROM TourExpense d WHERE d.tourBooking.tourist.passportNumber = :passportNumber ORDER BY d.spentDate DESC")
    List<TourExpense> findTouristHistory(@Param("passportNumber") String passportNumber);

    boolean existsByTourBooking(TourBooking tourBooking);
}
