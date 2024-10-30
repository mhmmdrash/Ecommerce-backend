package com.example.scalerdemo.controller;

import com.example.scalerdemo.dto.FakeStoreProductDto;
import com.example.scalerdemo.exceptions.ProductNotFoundException;
import com.example.scalerdemo.models.Product;
import com.example.scalerdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
    private ProductService service;

    ProductController(@Qualifier("selfProductService") ProductService service) {
        this.service = service;
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) throws ProductNotFoundException {
        Product product = service.getSingleProduct(id);

        return ResponseEntity.ok(product);
    }

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getAllProducts(
        @RequestParam("pageNumber") int pageNumber,
        @RequestParam("pageSize") int pageSize,
        @RequestParam("fieldName") String fieldName
    ) {
        Page<Product> products = service.getAllProducts(pageNumber, pageSize, fieldName);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/createProduct")
    public ResponseEntity<Product> createProduct(@RequestBody FakeStoreProductDto product) throws ProductNotFoundException {
        Product p = service.createProduct(product);
        return ResponseEntity.ok(p);
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long productId, @RequestBody FakeStoreProductDto productDto) throws ProductNotFoundException {
        Product p = service.updateProduct(productId, productDto);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Long> deleteProduct(@PathVariable("id") long productId) throws ProductNotFoundException {
        service.deleteProduct(productId);
        return ResponseEntity.ok(productId);
    }
}
