package travel.tourism.traveland_tourism_agency.domain;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tour_bookings")
@Getter
@Setter
@NoArgsConstructor
public class TourBooking {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tour_date")
    private LocalDate tourDate;

    @Enumerated(EnumType.STRING)
    private TourStatus status;

    private String destination;

    private int travellers;

    @ManyToOne
    @JoinColumn(name = "tourist_id")
    private Tourist tourist;

    @ManyToOne 
    @JoinColumn (name = "tour_guide_id")
    private TourGuide tourGuide;
}
