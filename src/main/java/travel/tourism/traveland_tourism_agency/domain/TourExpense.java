package travel.tourism.traveland_tourism_agency.domain;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "tour_expenses")
@Getter
@Setter
@NoArgsConstructor
public class TourExpense {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String item;

    private String notes;

    @Column(name = "amount_rwf")
    private int amountRwf;

    @Column(name = "spent_date")
    private LocalDate spentDate;

    @ManyToOne
    @JoinColumn(name = "tour_booking_id")
    private TourBooking tourBooking;
}
