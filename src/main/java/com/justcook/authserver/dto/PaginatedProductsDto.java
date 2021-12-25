package com.justcook.authserver.dto;

import com.justcook.authserver.model.Product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedProductsDto {
    Integer page;
    Integer recordsPerPage;
    Integer pageCount;
    List<Product> products;
}
