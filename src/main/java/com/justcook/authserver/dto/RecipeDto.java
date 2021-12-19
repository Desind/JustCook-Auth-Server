package com.justcook.authserver.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecipeDto {
    private String id;
    private String title;
    private LocalDateTime creationDate;
    private String owner;
    private String ownerUsername;
}
