package com.justcook.authserver.repository;

import com.justcook.authserver.model.Product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    Product findByEan(String ean);
    @Query("{ 'name': {'$regex' : ?0, '$options' : 'i'}}")
    List<Product> findByQuery(String regex);
    void deleteByEan(String ean);
}
