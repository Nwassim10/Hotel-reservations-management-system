package edu.miu.cs.cs544.service.impl;

import edu.miu.cs.cs544.cs544.RetryExtension;
import edu.miu.cs.cs544.domain.*;
import edu.miu.cs.cs544.domain.adapter.ProductAdapter;
import edu.miu.cs.cs544.domain.dto.ProductDTO;
import edu.miu.cs.cs544.repository.ProductRepository;
import edu.miu.cs.cs544.service.ProductService;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ExtendWith(RetryExtension.class)
@SpringBootTest
class ProductServiceImpTest {

    @TestConfiguration
    static class ProductServiceImpTestContextConfiguration {
        @Bean
        public ProductServiceImp productServiceImp() {
            return new ProductServiceImp();
        }
    }

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;


    @Test
    void addProduct() throws CustomError {
        double nightlyRate = 100;
        ProductDTO productDTO = new ProductDTO(2, "Test Product", "Test Product", "This is a test product", ProductType.Room, nightlyRate, 2, true, null);
        Mockito.when(productRepository.save(ProductAdapter.getProduct(productDTO))).thenReturn(ProductAdapter.getProduct(productDTO));
        productService.addProduct(productDTO);
        Mockito.verify(productRepository, Mockito.times(1)).save(ProductAdapter.getProduct(productDTO));
    }

    @Test
    void updateProduct() throws CustomError {
        //save a product
        double nightlyRate = 100;
        ProductDTO productDTO = new ProductDTO(2, "Test Product", "Test Product", "This is a test product", ProductType.Room, nightlyRate, 2, true, null);
        Mockito.when(productRepository.save(ProductAdapter.getProduct(productDTO))).thenReturn(ProductAdapter.getProduct(productDTO));
        ProductDTO savedProductDTO = productService.addProduct(productDTO);
        Mockito.verify(productRepository, Mockito.times(1)).save(ProductAdapter.getProduct(productDTO));

        //update the product
        savedProductDTO.setName("Updated Test Product");
        savedProductDTO.setDescription("Updated Test Product");

        Mockito.when(productRepository.save(ProductAdapter.getProduct(savedProductDTO))).thenReturn(ProductAdapter.getProduct(savedProductDTO));
        Mockito.when(productRepository.findById(savedProductDTO.getId())).thenReturn(Optional.of(ProductAdapter.getProduct(savedProductDTO)));
        ProductDTO updateProductDTO = productService.updateProduct(savedProductDTO.getId(), savedProductDTO);
        Mockito.verify(productRepository, Mockito.times(1)).save(ProductAdapter.getProduct(savedProductDTO));

        assertEquals(updateProductDTO.getName(), "Updated Test Product");
        assertEquals(updateProductDTO.getDescription(), "Updated Test Product");

    }

    @Test
    void deleteProduct() throws CustomError {
        double nightlyRate = 100;
        Product product = new Product(1, "Test Product", "Test Product", "This is a test product", ProductType.Room, nightlyRate, 2, true, null);
        Optional<Product> optionalProduct = Optional.of(product);
        Mockito.when(productRepository.findById(product.getId())).thenReturn(optionalProduct);
        productService.deleteProduct(1);
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    void getProduct() throws CustomError {
        double nightlyRate = 100;
        Product product = new Product(1, "Test Product", "Test Product", "This is a test product", ProductType.Room, nightlyRate, 2, true, null);
        Optional<Product> optionalProduct = Optional.of(product);
        Mockito.when(productRepository.findById(product.getId())).thenReturn(optionalProduct);
        ProductDTO found = productService.getProduct(1);
        assertEquals(found.getId(), 1);
    }

    @Test
    void getAllProducts() {
        // Arrange
        double nightlyRate = 100;
        Product product1 = new Product(1, "Test Product 1", "Test Product 1", "This is a test product 1", ProductType.Room, nightlyRate, 2, true, null);
        Product product2 = new Product(2, "Test Product 2", "Test Product 2", "This is a test product 2", ProductType.Room, nightlyRate, 2, true, null);
        List<Product> productList = List.of(product1, product2);
        Mockito.when(productRepository.findAll()).thenReturn(productList);

        // Act
        List<ProductDTO> productDTOList = productService.getAllProducts();

        // Assert
        assertEquals(productList.size(), productDTOList.size());
        for (int i = 0; i < productList.size(); i++) {
            assertEquals(productList.get(i).getId(), productDTOList.get(i).getId());
            assertEquals(productList.get(i).getName(), productDTOList.get(i).getName());
            assertEquals(productList.get(i).getExcerpt(), productDTOList.get(i).getExcerpt());
            assertEquals(productList.get(i).getDescription(), productDTOList.get(i).getDescription());
            assertEquals(productList.get(i).getMaxCapacity(), productDTOList.get(i).getMaxCapacity());
            assertEquals(productList.get(i).getNightlyRate(), productDTOList.get(i).getNightlyRate(), 0.01);
            assertEquals(productList.get(i).getIsAvailable(), productDTOList.get(i).getIsAvailable());

        }
    }

    @Test
    void getAllAvailableProducts() {
        // Arrange
        double nightlyRate = 100;
        Product product1 = new Product(1, "Test Product 1", "Test Product 1", "This is a test product 1", ProductType.Room, nightlyRate, 2, true, null);
        Product product2 = new Product(2, "Test Product 2", "Test Product 2", "This is a test product 2", ProductType.Room, nightlyRate, 2, true, null);
        List<Product> productList = List.of(product1, product2);
        Mockito.when(productRepository.findAll()).thenReturn(productList);

        // Act
        List<ProductDTO> productDTOList = productService.getAllAvailableProducts();

        // Assert
        assertEquals(productList.size(), productDTOList.size());
        for (int i = 0; i < productList.size(); i++) {
            assertEquals(productList.get(i).getId(), productDTOList.get(i).getId());
            assertEquals(productList.get(i).getName(), productDTOList.get(i).getName());
            assertEquals(productList.get(i).getExcerpt(), productDTOList.get(i).getExcerpt());
            assertEquals(productList.get(i).getDescription(), productDTOList.get(i).getDescription());
            assertEquals(productList.get(i).getMaxCapacity(), productDTOList.get(i).getMaxCapacity());
            assertEquals(productList.get(i).getNightlyRate(), productDTOList.get(i).getNightlyRate(), 0.01);
            assertEquals(productList.get(i).getIsAvailable(), productDTOList.get(i).getIsAvailable());

        }
    }
}