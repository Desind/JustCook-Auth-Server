package com.justcook.authserver.repository;

import com.justcook.authserver.model.Product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    Product findByEan(String ean);
}
