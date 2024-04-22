package edu.miu.cs.cs544.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String line1;

    private String line2;

    private String city;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private State state;

    private String postalCode;

    @Embedded
    private AuditData auditData;

}
