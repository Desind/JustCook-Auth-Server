package com.justcook.authserver.service.interfaces;

import com.justcook.authserver.model.Allergens;
import com.justcook.authserver.model.Recipe.CategoryCuisineForm;
import com.justcook.authserver.model.Recipe.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> getAllRecipes(int page, int records);
    Recipe createNewRecipe(Recipe recipe);
    Recipe getRecipeById(String id);
    List<Recipe> getRecipesByOwner(String id);
    List<Recipe> getRecipesWithoutAlergens(List<Allergens> alergens, int page, int records);
    List<Recipe> getRecipesWithCategoryAndCuisine(CategoryCuisineForm form);
    List<Recipe> getRecipesWithIngredients(List<String> ingredients);
}
