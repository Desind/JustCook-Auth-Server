package com.justcook.authserver.dto;

import com.justcook.authserver.model.Recipe.Recipe;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedRecipeDto {
    Integer page;
    Integer recordsPerPage;
    Integer pageCount;
    List<Recipe> recipes;
}
