package edu.miu.cs.cs544.controller;

import edu.miu.cs.cs544.domain.CustomError;
import edu.miu.cs.cs544.domain.dto.ProductDTO;
import edu.miu.cs.cs544.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDTO productDTO) throws CustomError {
        return new ResponseEntity<>(productService.addProduct(productDTO), HttpStatus.CREATED);
    }


    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable int id, @Valid @RequestBody ProductDTO productDTO) throws CustomError {
        return new ResponseEntity<>(productService.updateProduct(id, productDTO), HttpStatus.OK);
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) throws CustomError {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable int id) throws CustomError {
        return new ResponseEntity<>(productService.getProduct(id), HttpStatus.OK);
    }


    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    private String getEmailFromAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication ==null) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            Map<String, Object> attributes = jwtAuthenticationToken.getTokenAttributes();
            return (String) attributes.get("email");
        }
        return null;
    }


}
