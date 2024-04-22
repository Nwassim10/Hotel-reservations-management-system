package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.Address;
import edu.miu.cs.cs544.domain.Customer;
import edu.miu.cs.cs544.domain.Reservation;
import edu.miu.cs.cs544.domain.RoleType;
import edu.miu.cs.cs544.domain.dto.AddressDTO;
import edu.miu.cs.cs544.domain.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {

    public CustomerDTO createCustomer(CustomerDTO customerDTO);
    public List<CustomerDTO> getAllCustomers();
    public CustomerDTO getCustomerById(Integer customerId);
    CustomerDTO updateCustomer(Integer customerId, CustomerDTO updatedCustomer);
    public List<Reservation> getReservationsForCustomer(Integer customerId);
    public void deleteCustomer(Integer customerId);

}
