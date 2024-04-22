package edu.miu.cs.cs544.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private String excerpt;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    private double nightlyRate;

    private int maxCapacity;

    private Boolean isAvailable;

    @Embedded
    private AuditData auditData;


}
