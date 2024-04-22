package edu.miu.cs.cs544.domain.dto;

import edu.miu.cs.cs544.domain.AuditData;
import edu.miu.cs.cs544.domain.ProductType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private String description;

    private String excerpt;
    @NotNull
    private ProductType type;

    @Min(value = 0, message = "Nightly rate must be a positive value")
    private double nightlyRate;

    @Min(value = 0, message = "Max capacity must be a positive value")
    private int maxCapacity;

    private Boolean isAvailable;

    private AuditData auditData;
}
