package com.justcook.authserver.service.interfaces;

import com.justcook.authserver.model.Recipe.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> getAllRecipes();
    Recipe createNewRecipe(Recipe recipe);
    Recipe getRecipeById(String id);
    List<Recipe> getRecipesByOwner(String id);
}
