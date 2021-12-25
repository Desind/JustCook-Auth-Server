package com.justcook.authserver.service.interfaces;

import com.justcook.authserver.dto.PaginatedProductsDto;
import com.justcook.authserver.model.Product.Product;

public interface ProductService {
    Product findProductByEan(String ean);
    Product addProduct(Product product);
    PaginatedProductsDto paginatedProducts(Integer page, Integer pageSize, String name);
    void deleteProduct(String id);
}
