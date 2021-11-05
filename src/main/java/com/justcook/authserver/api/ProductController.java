package com.justcook.authserver.api;


import com.justcook.authserver.model.Product.Product;
import com.justcook.authserver.service.interfaces.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/product")
@AllArgsConstructor
@Slf4j
public class ProductController {

    public final ProductService productService;

    @GetMapping("/{ean}")
    public ResponseEntity<Product> getProductByEan(@PathVariable String ean){
        return ResponseEntity.status(200).body(productService.findProductByEan(ean));
    }

    @PostMapping("/new")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        return ResponseEntity.status(201).body(productService.addProduct(product));
    }
}
