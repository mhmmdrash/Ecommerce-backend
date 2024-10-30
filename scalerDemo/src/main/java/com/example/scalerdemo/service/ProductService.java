package com.example.scalerdemo.service;

import com.example.scalerdemo.dto.FakeStoreProductDto;
import com.example.scalerdemo.exceptions.ProductNotFoundException;
import com.example.scalerdemo.models.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product getSingleProduct(Long productId) throws ProductNotFoundException;
    Page<Product> getAllProducts(int pageSize, int pageNumber, String fieldName);
    Product createProduct(FakeStoreProductDto product);
    Product updateProduct(Long productId, FakeStoreProductDto product) throws ProductNotFoundException;
    Long deleteProduct(Long ProductId) throws ProductNotFoundException;
}
