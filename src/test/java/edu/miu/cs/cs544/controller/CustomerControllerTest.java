package edu.miu.cs.cs544.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.miu.cs.cs544.domain.ProductType;
import edu.miu.cs.cs544.domain.ReservationType;
import edu.miu.cs.cs544.domain.RoleType;
import edu.miu.cs.cs544.domain.dto.*;
import edu.miu.cs.cs544.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testUser", roles = "CLIENT")
    public void createCustomerTest() throws Exception {

        CustomerDTO customerDTO = createDummyCustomerDTO();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                        .content(new ObjectMapper().writeValueAsString(customerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Mockito.verify(customerService, Mockito.times(1)).createCustomer(customerDTO);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "CLIENT")
    void updateCustomerTest() throws Exception {
        CustomerDTO customerDTO = createDummyCustomerDTO();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/customers/2")
                        .content(new ObjectMapper().writeValueAsString(customerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(customerService, Mockito.times(1)).updateCustomer(2, customerDTO);
    }


    @Test
    @WithMockUser(username = "testUser", roles = "CLIENT")
    void getAllProductsTest() throws Exception {

        CustomerDTO customerDTO1 = createDummyCustomerDTO();
        customerDTO1.setId(1);
        CustomerDTO customerDTO2 = createDummyCustomerDTO();
        customerDTO2.setId(2);
        List<CustomerDTO> customerDTOList = List.of(customerDTO1, customerDTO2);
        Mockito.when(customerService.getAllCustomers()).thenReturn(customerDTOList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));
    }


    @Test
    @WithMockUser(username = "testUser", roles = "CLIENT")
    void deleteCustomerTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/customers/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(customerService, Mockito.times(1)).deleteCustomer(1);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "CLIENT")
    void getCustomerTest() throws Exception {
        CustomerDTO customerDTO = createDummyCustomerDTO();
        Mockito.when(customerService.getCustomerById(1)).thenReturn(customerDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
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