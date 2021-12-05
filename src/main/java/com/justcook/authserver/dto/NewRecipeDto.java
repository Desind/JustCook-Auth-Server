package com.justcook.authserver.dto;

import com.justcook.authserver.model.Allergens;
import com.justcook.authserver.model.Recipe.RecipeCategory;
import com.justcook.authserver.model.Recipe.RecipeCuisine;
import com.justcook.authserver.model.Recipe.RecipeDifficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
@NonNull
public class NewRecipeDto {
    private String title;
    private String description;
    private List<String> ingredients;
    private List<String> steps;
    private List<Allergens> allergens;
    private List<RecipeCategory> categories;
    private List<RecipeCuisine> cuisines;
    private Integer duration;
    private List<String> images;
    private RecipeDifficulty difficulty;
}
