package com.justcook.authserver.dto;

import lombok.Data;
import java.util.List;

@Data
public class RecipeIngredientsDto {
    private String id;
    private List<String> ingredients;
}
