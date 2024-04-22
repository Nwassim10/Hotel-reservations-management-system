package edu.miu.cs.cs544.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.miu.cs.cs544.cs544.RetryExtension;
import edu.miu.cs.cs544.domain.ProductType;
import edu.miu.cs.cs544.domain.ReservationType;
import edu.miu.cs.cs544.domain.adapter.ReservationAdapter;
import edu.miu.cs.cs544.domain.dto.AuditDataDTO;
import edu.miu.cs.cs544.domain.dto.ItemDTO;
import edu.miu.cs.cs544.domain.dto.ProductDTO;
import edu.miu.cs.cs544.domain.dto.ReservationDTO;
import edu.miu.cs.cs544.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RetryExtension.class)
class ReservationControllerTest {

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createReservation() throws Exception {
        double nightlyRate = 100;
        ProductDTO productDTO = new ProductDTO(2, "Test Product", "Test Product", "This is a test product", ProductType.Room, nightlyRate, 2, true, null);
        List<ItemDTO> itemDTOList = new ArrayList<>();
        ItemDTO itemDTO1 = new ItemDTO(1, 5, null, null, productDTO, null);
        itemDTOList.add(itemDTO1);
        ReservationDTO reservationDTO = new ReservationDTO(13, itemDTOList, null, ReservationType.NEW);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations")
                        .content(new ObjectMapper().writeValueAsString(reservationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Mockito.verify(reservationService, Mockito.times(1)).createReservation(reservationDTO);
    }

    @Test
    public void deleteReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservations/1")).andExpect(status().isOk());
        Mockito.verify(reservationService, Mockito.times(1)).deleteReservation(1);
    }

    @Test
    public void getReservationTest() throws Exception {
    ReservationDTO reservationDTO = createDummyReservationDTO(1);
    Integer reservationId = ReservationAdapter.getReservation(reservationDTO).getId();

        Mockito.when(reservationService.getReservation(reservationId)).thenReturn(reservationDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));


    }
    @Test
    public void getAllReservationTest() throws Exception {
    ReservationDTO reservationDTO = createDummyReservationDTO(1);
    ReservationDTO reservationDTO2 = createDummyReservationDTO(2);

    ReservationAdapter.getReservation(reservationDTO2).setId(2);

    List<ReservationDTO> reservationDTOList = List.of(reservationDTO,reservationDTO2);

        System.out.println(reservationDTOList);

        Mockito.when(reservationService.getAllReservation()).thenReturn(reservationDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));


    }
    private ReservationDTO createDummyReservationDTO(Integer reservationId) {
        // Create a dummy ItemDTO
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(1);
        itemDTO.setOccupants(2);
        itemDTO.setCheckinDate(LocalDate.now());
        itemDTO.setCheckoutDate(LocalDate.now().plusDays(3));

        // Create a dummy ProductDTO
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1);
        productDTO.setName("Sample Product");
        productDTO.setDescription("Sample Product Description");
        productDTO.setExcerpt("Sample Excerpt");
        productDTO.setType(ProductType.Apartment);
        productDTO.setNightlyRate(100.0);
        productDTO.setMaxCapacity(4);
        productDTO.setIsAvailable(true);

        // Set ProductDTO in ItemDTO
        itemDTO.setProduct(productDTO);

        // Create a dummy AuditDataDTO
        AuditDataDTO auditDataDTO = new AuditDataDTO();
        auditDataDTO.setCreatedBy("testUser");
        auditDataDTO.setCreatedOn(LocalDateTime.now());
        auditDataDTO.setUpdatedBy("testUser");
        auditDataDTO.setUpdatedOn(LocalDateTime.now());

        // Create a dummy ReservationDTO
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(reservationId);
        reservationDTO.setItems(Collections.singletonList(itemDTO));
        reservationDTO.setAuditData(auditDataDTO);
        reservationDTO.setReservationType(ReservationType.NEW);


        return reservationDTO;
    }


}