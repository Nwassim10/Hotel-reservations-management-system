package edu.miu.cs.cs544.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class ItemDTO {

    private Integer id;

    @NotNull
    private Integer occupants;

    @NotNull
    private LocalDate checkinDate;

    @NotNull
    private LocalDate checkoutDate;

//    private ReservationDTO reservationDTO;

    @NotNull
    private ProductDTO product;

    private AuditDataDTO auditData;
}
