package edu.miu.cs.cs544.service.impl;

import edu.miu.cs.cs544.domain.Customer;
import edu.miu.cs.cs544.domain.Reservation;
import edu.miu.cs.cs544.domain.RoleType;
import edu.miu.cs.cs544.domain.adapter.CustomerAdapter;
import edu.miu.cs.cs544.domain.dto.AddressDTO;
import edu.miu.cs.cs544.domain.dto.CustomerDTO;
import edu.miu.cs.cs544.domain.dto.UserDTO;
import edu.miu.cs.cs544.repository.CustomerRepository;
import edu.miu.cs.cs544.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static edu.miu.cs.cs544.domain.adapter.AddressAdapter.getAddress;
import static edu.miu.cs.cs544.domain.adapter.CustomerAdapter.getCustomer;
import static edu.miu.cs.cs544.domain.adapter.CustomerAdapter.getCustomerDTO;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        // Use the CustomerAdapter to convert the CustomerDTO to a Customer entity
        Customer customer = CustomerAdapter.getCustomer(customerDTO);

        // Save the customer entity to the repository and convert the result back to a DTO
        return CustomerAdapter.getCustomerDTO(customerRepository.save(customer));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(CustomerAdapter::getCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(Integer customerId) {
        return getCustomerDTO(customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + customerId)));
    }

    @Override
    public CustomerDTO updateCustomer(Integer customerId, CustomerDTO updatedCustomer) {
        Optional<Customer> existingCustomerOptional = customerRepository.findById(customerId);

        if (existingCustomerOptional.isPresent()) {
            Customer existingCustomer = existingCustomerOptional.get();
            // Update fields as needed
            existingCustomer.setFirstName(updatedCustomer.getFirstName());
            existingCustomer.setLastName(updatedCustomer.getLastName());
            existingCustomer.setEmail(updatedCustomer.getEmail());
            //existingCustomer.setCustomerBillingAddress(getAddress(updatedCustomer.getCustomerBillingAddressDTO()));
            //existingCustomer.setCustomerPhysicalAddress(getAddress(updatedCustomer.getCustomerPhysicalAddressDTO()));

            // Save the updated customer
            return getCustomerDTO(customerRepository.save(existingCustomer));
        } else {
            // Handle customer not found
            throw new RuntimeException("Customer not found with id: " + customerId);
        }
    }

    @Override
    public List<Reservation> getReservationsForCustomer(Integer customerId) {
        Optional<Customer> customer = Optional.ofNullable(customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + customerId)));
        return customer.orElseThrow().getReservationList();
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        customerRepository.deleteById(customerId);
    }


}
