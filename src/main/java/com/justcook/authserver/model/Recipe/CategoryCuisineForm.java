package com.justcook.authserver.model.Recipe;

import lombok.Data;

import java.util.List;

@Data
public class CategoryCuisineForm {
    private List<RecipeCuisine> cuisines;
    private List<RecipeCategory> categories;
}
