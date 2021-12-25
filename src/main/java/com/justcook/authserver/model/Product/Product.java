package com.justcook.authserver.model.Product;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class Product {
    @Id
    private String id;
    @Indexed(unique = true)
    private String ean;
    private String name;
    private LocalDateTime creationDate;
}
