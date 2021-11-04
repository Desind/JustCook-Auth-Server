package com.justcook.authserver.service;

import com.justcook.authserver.model.Product.Product;
import com.justcook.authserver.repository.ProductRepository;
import com.justcook.authserver.service.interfaces.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    @Override
    public Product findProductByEan(String ean) {
        return productRepository.findByEan(ean);
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.insert(product);
    }
}
