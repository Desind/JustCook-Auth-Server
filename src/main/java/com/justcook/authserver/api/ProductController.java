package com.justcook.authserver.api;


import com.justcook.authserver.dto.PaginatedProductsDto;
import com.justcook.authserver.model.Product.Product;
import com.justcook.authserver.service.interfaces.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class ProductController {

    public final ProductService productService;

    @GetMapping("/product/{ean}")
    public ResponseEntity<Product> getProductByEan(@PathVariable String ean){
        Optional<Product> product = productService.findProductByEan(ean);
        return product.map(value -> ResponseEntity.status(200).body(value)).orElseGet(() -> ResponseEntity.status(204).build());
    }

    @PostMapping("/product")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        return ResponseEntity.status(201).body(productService.addProduct(product));
    }

    @GetMapping("/products")
    public ResponseEntity<PaginatedProductsDto> paginatedProducts(@RequestParam String name,
                                                                  @RequestParam(defaultValue = "1") Integer page,
                                                                  @RequestParam(defaultValue = "10") Integer pageSize){
        return ResponseEntity.status(200).body(productService.paginatedProducts(page,pageSize,name));
    }

    @DeleteMapping("/product/{ean}")
    public ResponseEntity<?> deleteProduct(@PathVariable String ean){
        productService.deleteProduct(ean);
        return ResponseEntity.status(200).build();
    }

}














