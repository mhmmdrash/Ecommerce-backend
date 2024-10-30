package com.example.scalerdemo.service;

import com.example.scalerdemo.dto.FakeStoreProductDto;
import com.example.scalerdemo.exceptions.ProductNotFoundException;
import com.example.scalerdemo.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService {

    private RestTemplate restTemplate;
    private RedisTemplate redisTemplate;
    private String URL;

    public FakeStoreProductService(RestTemplate restTemplate, RedisTemplate redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
        this.URL = "https://fakestoreapi.com/";
    }

    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {

        Product cachedProduct = (Product) redisTemplate.opsForHash().get("PRODUCTS", "PRODUCTS_" + productId);
        if (cachedProduct != null) return cachedProduct;

        FakeStoreProductDto p =
            restTemplate.getForObject(
                URL + "products/" + productId,
                FakeStoreProductDto.class
            );

        if (p == null) {
            throw new ProductNotFoundException();
        }

        redisTemplate.opsForHash().put("PRODUCTS", "PRODUCTS_" + productId, p);

        return p.toProduct();
    }

    @Override
    public Page<Product> getAllProducts(int pageSize, int pageNumber, String fieldName) {
//        List<Product> products = new ArrayList<>();
//        String url = URL + "products";
//
//        if (sort.isPresent() || limit.isPresent()) {
//            url += "?";
//            if (sort.isPresent()) {
//                url += "sort=" + sort.get();
//                if (limit.isPresent()) url += "&";
//            }
//            if (limit.isPresent()) url += "limit=" + limit.get();
//        }
//
//        FakeStoreProductDto[] response = restTemplate.getForObject(
//            url,
//            FakeStoreProductDto[].class
//        );
//
//        for (FakeStoreProductDto p: response) {
//            products.add(p.toProduct());
//        }

//        return products;
        return null;
    }

    @Override
    public Product createProduct(FakeStoreProductDto product) {
        FakeStoreProductDto response;

        try {
            response = restTemplate.postForObject(
                URL + "products",
                product,
                FakeStoreProductDto.class
            );
        } catch(Exception e) {
            throw new RuntimeException("Create Product failed", e);
        }

        return response.toProduct();
    }

    @Override
    public Product updateProduct(Long productId, FakeStoreProductDto product) {
        String url = URL + "products/" + productId;
        try {
            restTemplate.put(url, product);
        } catch(Exception e) {
            return null;
//            throw new RuntimeException("Updation Failed", e);
        }
        return product.toProduct();
    }

    @Override
    public Long deleteProduct(Long productId) {
        String url = URL + "products/" + productId;
        try {
            restTemplate.delete(url);
        } catch (Exception e) {
            throw new RuntimeException("Deletion failed", e);
        }
        return productId;
    }
}
