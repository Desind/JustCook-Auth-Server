package com.justcook.authserver.api;

import com.justcook.authserver.model.Recipe.Recipe;
import com.justcook.authserver.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/recipe")
@AllArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/all")
    public ResponseEntity<List<Recipe>> getAllRecipes(){
        List<Recipe> allRecipes = recipeService.getAllRecipes();
        return ResponseEntity.status(200).body(allRecipes);
    }

    @PostMapping("/new")
    public ResponseEntity<Object> createNewRecipe(@RequestBody Recipe recipe){
        System.out.println(recipe.toString());
        recipeService.createNewRecipe(recipe);
        return ResponseEntity.status(201).build();
    }
}
