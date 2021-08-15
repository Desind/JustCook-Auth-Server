package com.justcook.authserver.service;

import com.justcook.authserver.model.Recipe.Recipe;
import com.justcook.authserver.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe createNewRecipe(Recipe recipe) {
        return recipeRepository.insert(recipe);
    }
}
