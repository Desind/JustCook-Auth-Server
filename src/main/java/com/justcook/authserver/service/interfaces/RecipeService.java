package com.justcook.authserver.service.interfaces;

import com.justcook.authserver.dto.NewRecipeDto;
import com.justcook.authserver.dto.PaginatedRecipeDto;
import com.justcook.authserver.dto.RecipeDto;
import com.justcook.authserver.model.Allergens;
import com.justcook.authserver.dto.CategoryCuisineDto;
import com.justcook.authserver.model.Recipe.Recipe;
import com.justcook.authserver.model.Recipe.RecipeCategory;
import com.justcook.authserver.model.Recipe.RecipeCuisine;

import java.util.List;

public interface RecipeService {
    List<Recipe> getAllRecipes(int page, int records);
    Recipe createNewRecipe(NewRecipeDto recipe, String owner);
    Recipe getRecipeById(String id);
    List<Recipe> getRecipesByOwner(String id);
    List<Recipe> getRecipesWithoutAlergens(List<Allergens> alergens, int page, int records);
    List<Recipe> getRecipesWithCategoryAndCuisine(CategoryCuisineDto form);
    PaginatedRecipeDto getRecipesWithIngredients(List<String> ingredients, Integer page, Integer pagesize);
    List<RecipeCategory> getRecipeCategories();
    List<RecipeCuisine> getRecipeCuisines();
    List<Allergens> getAllergens();
    List<RecipeDto> getAdminRecipes(String title, String owner);
    PaginatedRecipeDto recommendRecipes(String email, Integer page, Integer pagesize);
    void deleteRecipe(String id);
}
