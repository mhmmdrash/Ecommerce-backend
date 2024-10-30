package com.example.scalerdemo.service;

import com.example.scalerdemo.dto.FakeStoreProductDto;
import com.example.scalerdemo.exceptions.ProductNotFoundException;
import com.example.scalerdemo.models.Category;
import com.example.scalerdemo.models.Product;
import com.example.scalerdemo.repositories.CategoryRepository;
import com.example.scalerdemo.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("selfProductService")
public class SelfProductService implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public SelfProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findById(productId);
        if (!product.isPresent()) throw new ProductNotFoundException();
        return product.get();
    }

    @Override
    public Page<Product> getAllProducts(int pageSize, int pageNumber, String fieldName) {
        Page<Product> allProducts = productRepository.findAll(PageRequest.of(pageSize, pageNumber, Sort.by(fieldName).ascending()));
        return allProducts;
    }

    @Override
    public Product createProduct(FakeStoreProductDto product) {
        Product newProduct = product.toProduct();
        Category category = categoryRepository.findByTitle(newProduct.getCategory().getTitle());

        if (category == null) {
            Category newCategory = newProduct.getCategory();
            newCategory = categoryRepository.save(newCategory);
            newProduct.setCategory(newCategory);
        } else newProduct.setCategory(category);

        Product response = productRepository.save(newProduct);
        return response;
    }

    @Override
    public Product updateProduct(Long productId, FakeStoreProductDto product) throws ProductNotFoundException {
        Product updatedProduct = product.toProduct();

        Optional<Product> p = productRepository.findById(productId);
        if (!p.isPresent()) throw new ProductNotFoundException();

        Product currentProduct = p.get();

        if (updatedProduct.getCategory().getTitle() != null) {
            Category category = categoryRepository.findByTitle(updatedProduct.getCategory().getTitle());
            if (category == null) {
                Category newCategory = updatedProduct.getCategory();
                newCategory = categoryRepository.save(newCategory);
                currentProduct.setCategory(newCategory);
            } else currentProduct.setCategory(category);
        }


        if (updatedProduct.getTitle() != null) {
            currentProduct.setTitle(updatedProduct.getTitle());
        }
        if (updatedProduct.getDescription() != null) {
            currentProduct.setDescription(updatedProduct.getDescription());
        }
        if (updatedProduct.getPrice() != 0) {
            currentProduct.setPrice(updatedProduct.getPrice());
        }
        if (updatedProduct.getImageUrl() != null) {
            currentProduct.setImageUrl(updatedProduct.getImageUrl());
        }

        productRepository.save(currentProduct);
        return currentProduct;
    }

    @Override
    public Long deleteProduct(Long productId) throws ProductNotFoundException {
        try {
            productRepository.deleteById(productId);
        } catch(Exception e) {
            throw new ProductNotFoundException();
        }

        return productId;
    }
}
