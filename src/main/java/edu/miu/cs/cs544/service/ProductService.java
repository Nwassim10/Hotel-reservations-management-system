package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.CustomError;
import edu.miu.cs.cs544.domain.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO addProduct(ProductDTO productDTO) throws CustomError;

    ProductDTO updateProduct(int id, ProductDTO productDTO) throws CustomError;

    void deleteProduct(int id) throws CustomError;

    ProductDTO getProduct(int id) throws CustomError;

    List<ProductDTO> getAllProducts();

    List<ProductDTO> getAllAvailableProducts();
}
