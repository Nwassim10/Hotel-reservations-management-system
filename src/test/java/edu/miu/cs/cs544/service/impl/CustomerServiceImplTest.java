package edu.miu.cs.cs544.service.impl;

import edu.miu.cs.cs544.cs544.RetryExtension;
import edu.miu.cs.cs544.domain.*;
import edu.miu.cs.cs544.domain.adapter.CustomerAdapter;
import edu.miu.cs.cs544.domain.dto.*;
import edu.miu.cs.cs544.repository.CustomerRepository;
import edu.miu.cs.cs544.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@ExtendWith(RetryExtension.class)
@SpringBootTest
class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;


    @TestConfiguration
    static class CustomerServiceImpTestContextConfiguration {
        @Bean
        public CustomerServiceImpl customerService() {
            return new CustomerServiceImpl();
        }
    }

    @Test
    void createCustomerTest() throws CustomError{
        CustomerDTO customerDTO = createDummyCustomerDTO();
        Mockito.when(customerRepository.save(CustomerAdapter.getCustomer(customerDTO)))
                .thenReturn(CustomerAdapter.getCustomer(customerDTO));
        customerService.createCustomer(customerDTO);
        Mockito.verify(customerRepository, Mockito.times(1)).save(CustomerAdapter.getCustomer(customerDTO));
    }

    @Test
    void updateCustomerTest() throws CustomError {
        CustomerDTO customerDTO = createDummyCustomerDTO();

        Mockito.when(customerRepository.save(CustomerAdapter.getCustomer(customerDTO)))
                .thenReturn(CustomerAdapter.getCustomer(customerDTO));
        CustomerDTO savedCustomerDTO = customerService.createCustomer(customerDTO);
        Mockito.verify(customerRepository, Mockito.times(1))
                .save(CustomerAdapter.getCustomer(customerDTO));

        savedCustomerDTO.setFirstName("New_First_Name");
        savedCustomerDTO.setLastName("New_Last_Name");

        Mockito.when(customerRepository.save(CustomerAdapter.getCustomer(savedCustomerDTO)))
                .thenReturn(CustomerAdapter.getCustomer(savedCustomerDTO));
        Mockito.when(customerRepository.findById(savedCustomerDTO.getId()))
                .thenReturn(Optional.of(CustomerAdapter.getCustomer(savedCustomerDTO)));
        CustomerDTO updatedCustomerDTO = customerService.updateCustomer(savedCustomerDTO.getId(), savedCustomerDTO);
        Mockito.verify(customerRepository, Mockito.times(1))
                .save(CustomerAdapter.getCustomer(customerDTO));
        assertEquals(updatedCustomerDTO.getFirstName(), "New_First_Name");
        assertEquals(updatedCustomerDTO.getLastName(), "New_Last_Name");
    }


    @Test
    void deleteCustomerTest() throws CustomError {
        Customer customer = CustomerAdapter.getCustomer(createDummyCustomerDTO());
        Optional<Customer> optionalCustomer = Optional.of(customer);
        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(optionalCustomer);
        customerService.deleteCustomer(1);
        Mockito.verify(customerRepository, Mockito.times(1)).deleteById(1);
    }


    @Test
    void getCustomerTest() throws CustomError {
        Customer customer = CustomerAdapter.getCustomer(createDummyCustomerDTO());
        Optional<Customer> optionalCustomer = Optional.of(customer);
        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(optionalCustomer);
        CustomerDTO foundCustomerDTO = customerService.getCustomerById(1);
        assertEquals(foundCustomerDTO.getId(),1);
    }


    private CustomerDTO createDummyCustomerDTO() {

        AuditDataDTO auditDataDTO = new AuditDataDTO();
        auditDataDTO.setCreatedBy("Person A");
//        auditDataDTO.setCreatedOn(LocalDateTime.now());
        auditDataDTO.setUpdatedBy("Person A");
//        auditDataDTO.setUpdatedOn(LocalDateTime.now());

        StateDTO stateDTO = new StateDTO();
        stateDTO.setAuditDataDTO(auditDataDTO);
        stateDTO.setCode("53333");
        stateDTO.setId(1);
        stateDTO.setName("Texas");

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStateDTO(stateDTO);
        addressDTO.setId(1);
        addressDTO.setAuditData(auditDataDTO);
        addressDTO.setLine1("123 Ave south");
        addressDTO.setPostalCode("53333");


        String firstName = "John";
        String lastName = "Doe";
        String email = "johndoe@gmail.com";

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1);
        productDTO.setName("Room");
        productDTO.setDescription("Beautiful Room");
        productDTO.setExcerpt("Beautiful Room");
        productDTO.setAuditData(null);
        productDTO.setType(ProductType.Room);
        productDTO.setMaxCapacity(10);
        productDTO.setNightlyRate(125.00);
        productDTO.setIsAvailable(true);


        ItemDTO itemDTO = new ItemDTO(1, 5, null, null, productDTO, auditDataDTO);

        List<ItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(itemDTO);
        ReservationDTO reservationDTO = new ReservationDTO(1, itemDTOList, auditDataDTO, ReservationType.NEW);
        List<ReservationDTO> reservationDTOList = new ArrayList<>();
        reservationDTOList.add(reservationDTO);
        String userName = "userName";
        String userPass = "userPass";
        UserDTO userDTO = new UserDTO(1, userName, userPass, true, auditDataDTO, RoleType.CLIENT);


        return new CustomerDTO(1, firstName, lastName, email, auditDataDTO, addressDTO, addressDTO, reservationDTOList, userDTO, userPass, userName, RoleType.CLIENT);
    }

}