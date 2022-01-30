package com.justcook.authserver.service;

import com.justcook.authserver.dto.PaginatedProductsDto;
import com.justcook.authserver.model.Product.Product;
import com.justcook.authserver.model.Recipe.Recipe;
import com.justcook.authserver.repository.ProductRepository;
import com.justcook.authserver.service.interfaces.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Optional<Product> findProductByEan(String ean) {
        Product product = productRepository.findByEan(ean);
        return Optional.ofNullable(product);
    }

    @Override
    public Product addProduct(Product product) {
        product.setCreationDate(LocalDateTime.now());
        return productRepository.insert(product);
    }

    @Override
    public PaginatedProductsDto paginatedProducts(Integer page, Integer pageSize, String name){
        List<Product> products = productRepository.findByQuery(name);
        products.sort(Comparator.comparing(Product::getCreationDate).reversed());
        Integer pagesCount = (int) Math.ceil(products.size()/(double)pageSize);
        if(page>pagesCount){
            page = pagesCount;
        }if(page<1){
            pagesCount=1;
            page=1;
        }
        return new PaginatedProductsDto(page,
                pageSize,
                pagesCount,
                products.stream().skip((page-1)*pageSize).limit(pageSize).collect(Collectors.toList()));
    }

    @Override
    public void deleteProduct(String ean) {
        productRepository.deleteByEan(ean);
    }

}
