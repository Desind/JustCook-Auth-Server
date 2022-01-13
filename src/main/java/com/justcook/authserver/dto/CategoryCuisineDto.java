package com.justcook.authserver.dto;

import com.justcook.authserver.model.Recipe.RecipeCategory;
import com.justcook.authserver.model.Recipe.RecipeCuisine;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class CategoryCuisineDto {
    private List<RecipeCuisine> cuisines;
    private List<RecipeCategory> categories;
}
