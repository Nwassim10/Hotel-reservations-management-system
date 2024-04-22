package edu.miu.cs.cs544.domain.adapter;

import edu.miu.cs.cs544.domain.Product;
import edu.miu.cs.cs544.domain.dto.ProductDTO;

public class ProductAdapter {
    public static ProductDTO getProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getExcerpt(), product.getType(), product.getNightlyRate(), product.getMaxCapacity(), product.getIsAvailable(), product.getAuditData());
        return productDTO;
    }

    public static Product getProduct(ProductDTO productDTO) {
        Product product = new Product(productDTO.getId(), productDTO.getName(), productDTO.getDescription(), productDTO.getExcerpt(), productDTO.getType(), productDTO.getNightlyRate(), productDTO.getMaxCapacity(), productDTO.getIsAvailable(), productDTO.getAuditData());
        return product;
    }
}
