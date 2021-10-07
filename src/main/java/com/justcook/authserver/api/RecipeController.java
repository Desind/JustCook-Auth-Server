package com.justcook.authserver.api;

import com.justcook.authserver.model.Recipe.Recipe;
import com.justcook.authserver.service.RecipeServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/recipe")
@AllArgsConstructor
public class RecipeController {

    private final RecipeServiceImpl recipeService;

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

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable String id){
        Optional<Recipe> recipe = Optional.ofNullable(recipeService.getRecipeById(id));
        return recipe.map(value -> ResponseEntity.status(200).body(value)).orElseGet(() -> ResponseEntity.status(404).build());
    }

    @GetMapping("/owner/{id}")
    public ResponseEntity<List<Recipe>> getRecipesByOwner(@PathVariable String id){
        return ResponseEntity.status(200).body(recipeService.getRecipesByOwner(id));
    }

}
