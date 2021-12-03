package com.justcook.authserver.service;

import com.justcook.authserver.model.Allergens;
import com.justcook.authserver.dto.CategoryCuisineDto;
import com.justcook.authserver.model.Recipe.Recipe;
import com.justcook.authserver.repository.RecipeRepository;
import com.justcook.authserver.service.interfaces.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public List<Recipe> getAllRecipes(int page, int records) {
        return recipeRepository.findAll().stream().skip((page-1)*records).limit(records).collect(Collectors.toList());
    }

    @Override
    public Recipe createNewRecipe(Recipe recipe) {
        recipe.setCreationDate(LocalDateTime.now());
        return recipeRepository.insert(recipe);
    }

    @Override
    public Recipe getRecipeById(String id) {
        return recipeRepository.findRecipeById(id);
    }

    @Override
    public List<Recipe> getRecipesByOwner(String id) {
        return recipeRepository.findRecipesByOwner(id);
    }

    @Override
    public List<Recipe> getRecipesWithoutAlergens(List<Allergens> alergens, int page, int records){
        return recipeRepository.findRecipesByAllergensNotContains(alergens).stream().skip((page-1)*records).limit(records).toList();
    }

    @Override
    public List<Recipe> getRecipesWithCategoryAndCuisine(CategoryCuisineDto form) {
        if(form.getCuisines() != null && form.getCategories() == null){
            return recipeRepository.findRecipesByCuisinesContains(form.getCuisines());
        }else if(form.getCuisines() == null && form.getCategories() != null){
            return recipeRepository.findRecipesByCategoriesContains(form.getCategories());
        }else if(form.getCuisines() == null && form.getCategories() == null){
            return recipeRepository.findAll();
        }else{
            return recipeRepository.findRecipesByCategoriesContainsOrCuisinesContains(form.getCategories(),form.getCuisines());
        }
    }

    @Override
    public List<Recipe> getRecipesWithIngredients(List<String> ingredients) {
        return recipeRepository.findRecipesByIngredients(ingredients);
    }
}
