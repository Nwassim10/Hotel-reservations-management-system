package edu.miu.cs.cs544.domain.adapter;

import edu.miu.cs.cs544.domain.Customer;
import edu.miu.cs.cs544.domain.dto.CustomerDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerAdapter {
    public static Customer getCustomer(CustomerDTO customerDTO) {
        return new Customer(
                customerDTO.getId(),
                customerDTO.getFirstName(),
                customerDTO.getLastName(),
                customerDTO.getEmail(),
                AuditDataAdapter.getAuditData(customerDTO.getAuditDataDTO()),
                AddressAdapter.getAddress(customerDTO.getCustomerPhysicalAddressDTO()),
                AddressAdapter.getAddress(customerDTO.getCustomerBillingAddressDTO()),
                ReservationAdapter.getReservationList(customerDTO.getReservationDTOList()),
                UserAdapter.getUser(customerDTO.getUserDTO())
        );
    }

    public static CustomerDTO getCustomerDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                AuditDataAdapter.getAuditDataDTO(customer.getAuditData()),
                AddressAdapter.getAddressDTO(customer.getCustomerPhysicalAddress()),
                AddressAdapter.getAddressDTO(customer.getCustomerBillingAddress()),
                ReservationAdapter.getReservationDTOList(customer.getReservationList()),
                UserAdapter.getUserDTO(customer.getUser())
        );
    }
    private List<CustomerDTO> convertToDTOList(List<Customer> customers) {
        return customers.stream()
                .map(CustomerAdapter::getCustomerDTO)
                .collect(Collectors.toList());
    }
}
