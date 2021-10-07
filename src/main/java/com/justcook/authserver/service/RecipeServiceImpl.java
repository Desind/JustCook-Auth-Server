package com.justcook.authserver.service;

import com.justcook.authserver.model.Recipe.Recipe;
import com.justcook.authserver.repository.RecipeRepository;
import com.justcook.authserver.service.interfaces.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe createNewRecipe(Recipe recipe) {
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
}
