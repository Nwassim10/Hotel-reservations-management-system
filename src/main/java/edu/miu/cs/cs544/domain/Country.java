package edu.miu.cs.cs544.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;

    private String name;

    private Integer population;

    @Embedded
    private AuditData auditData;

    @OneToMany
    private List<State> stateList = new ArrayList<>();

}
