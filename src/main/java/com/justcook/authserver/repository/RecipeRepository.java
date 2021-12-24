package com.justcook.authserver.repository;

import com.justcook.authserver.dto.RecipeDto;
import com.justcook.authserver.dto.RecipeIngredientsDto;
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
    List<Recipe> findAllByOrderByCreationDateDesc();
    List<Recipe> findRecipesByAllergensNotContains(List<Allergens> alergens);
    List<Recipe> findRecipesByCategoriesContainsOrCuisinesContains(List<RecipeCategory> categories, List<RecipeCuisine> cuisines);
    List<Recipe> findRecipesByCuisinesContains(List<RecipeCuisine> cuisines);
    List<Recipe> findRecipesByCategoriesContains(List<RecipeCategory> categories);
    @Query("{ 'ingredients': {'$regex' : ?0, '$options' : 'i'}}")
    List<Recipe> findRecipesByIngredients(String ingredients);
    Integer countRecipesByOwner(String id);
    List<RecipeDto> findRecipesByTitleContainsAndOwnerIsLike(String title, String owner);
    @Query("{$and:[{title:{'$regex' : ?0, '$options' : 'i'}},{'allergens': {$nin: ?1}},{'categories': {$in: ?2}},{'cuisines': {$in: ?3}}]}")
    List<Recipe> queryRecipes(String title, List<Allergens> allergens, List<RecipeCategory> categories, List<RecipeCuisine> cuisines);
}
