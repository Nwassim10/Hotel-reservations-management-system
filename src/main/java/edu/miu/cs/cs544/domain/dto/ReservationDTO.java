package edu.miu.cs.cs544.domain.dto;

import edu.miu.cs.cs544.domain.ReservationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {

    private Integer id;

    @NotNull
    private List<ItemDTO> items;

    private AuditDataDTO auditData;

    private ReservationType reservationType;
}

