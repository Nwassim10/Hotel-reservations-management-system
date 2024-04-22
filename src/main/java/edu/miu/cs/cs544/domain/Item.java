package edu.miu.cs.cs544.domain;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer occupants;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

//	@ManyToOne(cascade = CascadeType.ALL)
//	private Reservation reservation;

    @ManyToOne
    private Product product;

    @Embedded
    private AuditData auditData;

}
