package com.justcook.authserver.repository;

import com.justcook.authserver.model.Allergens;
import com.justcook.authserver.model.Recipe.Recipe;
import com.justcook.authserver.model.Recipe.RecipeCategory;
import com.justcook.authserver.model.Recipe.RecipeCuisine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    List<Recipe> findRecipesByOwner(String id);
    Recipe findRecipeById(String id);
    List<Recipe> findAll();
    List<Recipe> findRecipesByAllergensNotContains(List<Allergens> alergens);
    List<Recipe> findRecipesByCategoriesContainsOrCuisinesContains(List<RecipeCategory> categories, List<RecipeCuisine> cuisines);
    List<Recipe> findRecipesByCuisinesContains(List<RecipeCuisine> cuisines);
    List<Recipe> findRecipesByCategoriesContains(List<RecipeCategory> categories);
    @Query("{ 'ingredients': {$all: ?0}}")
    List<Recipe> findRecipesByIngredients(List<String> ingredients);

}
