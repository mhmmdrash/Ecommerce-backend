package com.example.scalerdemo.repositories;

import com.example.scalerdemo.exceptions.ProductNotFoundException;
import com.example.scalerdemo.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
//    JPA Queries
    Product save(Product product);
    void deleteById(long ProductId);
    Product findByTitle(String title);
    Product findByDescription(String description);
    Product findByPrice(double price);
    Product findByImageUrl(String imageUrl);
    Page<Product> findAll(Pageable pageable);

    @Query(value = "select * from product p where p.title = :title", nativeQuery = true)
    Product getProductWithNativeQuery(@Param("title") String title);
}
