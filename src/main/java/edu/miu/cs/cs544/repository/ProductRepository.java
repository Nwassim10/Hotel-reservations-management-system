package edu.miu.cs.cs544.repository;

import edu.miu.cs.cs544.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
}
