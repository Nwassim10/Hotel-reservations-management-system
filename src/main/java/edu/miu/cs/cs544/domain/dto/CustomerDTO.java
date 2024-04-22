package edu.miu.cs.cs544.domain.dto;

import edu.miu.cs.cs544.domain.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Integer id;

    @Column(nullable = false)
    private String firstName;


    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    private AuditDataDTO auditDataDTO;

    private AddressDTO customerPhysicalAddressDTO;

    private AddressDTO customerBillingAddressDTO;

    private List<ReservationDTO> reservationDTOList = new ArrayList<>();

    private UserDTO userDTO;

    private String userPass;

    private  String userName;

    private RoleType roleType;


    public CustomerDTO(Integer id, String firstName, String lastName, String email, AuditDataDTO auditDataDTO, AddressDTO addressDTO, AddressDTO addressDTO1, List<ReservationDTO> reservationDTOList, UserDTO userDTO) {

   this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.auditDataDTO = auditDataDTO;
    this.customerPhysicalAddressDTO = addressDTO;
    this.customerBillingAddressDTO = addressDTO1;
    this.reservationDTOList = reservationDTOList;
    this.userDTO = userDTO;

    }
}
