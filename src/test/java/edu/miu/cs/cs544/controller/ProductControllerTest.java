package edu.miu.cs.cs544.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.miu.cs.cs544.cs544.RetryExtension;
import edu.miu.cs.cs544.domain.ProductType;
import edu.miu.cs.cs544.domain.dto.ProductDTO;
import edu.miu.cs.cs544.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(RetryExtension.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void addProduct() throws Exception {
        double nightlyRate = 100;
        ProductDTO productDTO = new ProductDTO(2, "Test Product", "Test Product", "This is a test product", ProductType.Room, nightlyRate, 2, true, null);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .content(new ObjectMapper().writeValueAsString(productDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        //verify
        Mockito.verify(productService, Mockito.times(1)).addProduct(productDTO);
    }

    @Test
    void updateProduct() throws Exception {
        double nightlyRate = 100;
        ProductDTO productDTO = new ProductDTO(2, "Test Product", "Test Product", "This is a test product", ProductType.Room, nightlyRate, 2, true, null);
        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/2")
                        .content(new ObjectMapper().writeValueAsString(productDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // Verify
        Mockito.verify(productService, Mockito.times(1)).updateProduct(2, productDTO);
    }

    @Test
    void deleteProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/2"))
                .andExpect(status().isOk());
        Mockito.verify(productService, Mockito.times(1)).deleteProduct(2);
    }

    @Test
    void getProduct() throws Exception {
        double nightlyRate = 100;
        ProductDTO productDTO = new ProductDTO(2, "Test Product", "Test Product", "This is a test product", ProductType.Room, nightlyRate, 2, true, null);
        Mockito.when(productService.getProduct(2)).thenReturn(productDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2));
    }

    @Test
    void getAllProducts() throws Exception {
        double nightlyRate = 100;
        ProductDTO productDTO1 = new ProductDTO(1, "Test Product1", "Test Product1", "This is a test product", ProductType.Room, nightlyRate, 2, true, null);
        ProductDTO productDTO2 = new ProductDTO(2, "Test Product2", "Test Product2", "This is a test product", ProductType.Apartment, nightlyRate, 2, true, null);
        List<ProductDTO> productDTOList = List.of(productDTO1, productDTO2);
        Mockito.when(productService.getAllProducts()).thenReturn(productDTOList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));
    }

}