package com.justcook.authserver.service.interfaces;

import com.justcook.authserver.dto.PaginatedProductsDto;
import com.justcook.authserver.model.Product.Product;

import java.util.Optional;

public interface ProductService {
    Optional<Product> findProductByEan(String ean);
    Product addProduct(Product product);
    PaginatedProductsDto paginatedProducts(Integer page, Integer pageSize, String name);
    void deleteProduct(String id);
}
