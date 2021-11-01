package com.justcook.authserver.api;

import com.justcook.authserver.model.Allergens;
import com.justcook.authserver.model.Recipe.CategoryCuisineForm;
import com.justcook.authserver.model.Recipe.Recipe;
import com.justcook.authserver.service.CookUserServiceImpl;
import com.justcook.authserver.service.RecipeServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/recipe")
@AllArgsConstructor
@Slf4j
public class RecipeController {

    private final RecipeServiceImpl recipeService;
    private final CookUserServiceImpl cookUserService;

    @GetMapping("/all/{page}")
    public ResponseEntity<List<Recipe>> getAllRecipes(@PathVariable Integer page){
        List<Recipe> allRecipes = recipeService.getAllRecipes(page,20);
        return ResponseEntity.status(200).body(allRecipes);
    }

    @PostMapping("/new")
    public ResponseEntity<Object> createNewRecipe(HttpServletRequest request, @RequestBody Recipe recipe){
        //TODO: PRZYJMOWANIE DTO TYLKO Z POLAMI POTRZEBNYMI
        String email = String.valueOf(request.getAttribute("username"));
        String userId = cookUserService.getCookUserByEmail(email).getId();
        recipe.setOwner(userId);
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

    @GetMapping("/recipes/{page}")
    public ResponseEntity<List<Recipe>> getRecipePagination(HttpServletRequest request, @PathVariable Integer page){
        String email = String.valueOf(request.getAttribute("username"));
        List<Allergens> alergens = cookUserService.getCookUserByEmail(email).getAllergies();
        return ResponseEntity.status(200).body(recipeService.getRecipesWithoutAlergens(alergens, page, 20));
    }

    @PostMapping("/test")
    public ResponseEntity<List<Recipe>> getTest(@RequestBody CategoryCuisineForm form){
        return ResponseEntity.status(200).body(recipeService.getRecipesWithCategoryAndCuisine(form));
    }


}
