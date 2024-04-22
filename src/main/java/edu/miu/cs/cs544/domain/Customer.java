package edu.miu.cs.cs544.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private AuditData auditData;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Address customerPhysicalAddress;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Address customerBillingAddress;

    @OneToMany(mappedBy = "customer")
    private List<Reservation> reservationList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private User user;


     private String userPass;

     private  String userName;


    public Customer(Integer id, String firstName, String lastName, String email, AuditData auditData, Address address, Address address1, List<Reservation> reservationList, User user) {

    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.auditData = auditData;
    this.customerPhysicalAddress = address;
    this.customerBillingAddress = address1;
    this.reservationList = reservationList;
    this.user = user;

    }
}
