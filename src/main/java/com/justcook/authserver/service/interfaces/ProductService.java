package com.justcook.authserver.service.interfaces;

import com.justcook.authserver.model.Product.Product;

public interface ProductService {
    Product findProductByEan(String ean);
    Product addProduct(Product product);
}
