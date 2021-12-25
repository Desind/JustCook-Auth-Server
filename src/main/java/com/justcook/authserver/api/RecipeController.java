package com.justcook.authserver.api;

import com.justcook.authserver.dto.NewRecipeDto;
import com.justcook.authserver.dto.PaginatedRecipeDto;
import com.justcook.authserver.model.Allergens;
import com.justcook.authserver.dto.CategoryCuisineDto;
import com.justcook.authserver.model.Recipe.Recipe;
import com.justcook.authserver.model.Recipe.RecipeCategory;
import com.justcook.authserver.model.Recipe.RecipeCuisine;
import com.justcook.authserver.model.User.CookUser;
import com.justcook.authserver.model.User.UserRole;
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

import io.swagger.annotations.*;

@Controller
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
@Api(value = "Recipe controller", tags = {"Recipes"})
public class RecipeController {

    private final RecipeServiceImpl recipeService;
    private final CookUserServiceImpl cookUserService;

    //200 - Correct output
    //400 - Invalid input
    //FETCH ALL RECIPES ORDERED BY DATE AND PAGINATED
    @GetMapping("/recipes/{page}")
    public ResponseEntity<List<Recipe>> getAllRecipes(@PathVariable Integer page){

        if (page < 1) {
            return ResponseEntity.status(400).build();
        }
        List<Recipe> allRecipes = recipeService.getAllRecipes(page,20);
        return ResponseEntity.status(200).body(allRecipes);
    }

    //201 - Correct insert
    //400 - Fields empty
    //ADD NEW RECIPE DO DATABASE
    @PostMapping("/recipe")
    public ResponseEntity<Recipe> createNewRecipe(HttpServletRequest request, @RequestBody NewRecipeDto recipe){
        String email = String.valueOf(request.getAttribute("username"));
        String userId = cookUserService.getCookUserByEmail(email).getId();
        Recipe createdRecipe = recipeService.createNewRecipe(recipe, userId);
        if(createdRecipe != null){
            return ResponseEntity.status(201).body(createdRecipe);
        }
        return ResponseEntity.status(400).build();
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable String id){
        Optional<Recipe> recipe = Optional.ofNullable(recipeService.getRecipeById(id));
        return recipe.map(value -> ResponseEntity.status(200).body(value)).orElseGet(() -> ResponseEntity.status(204).build());
    }

    @GetMapping("/owner-recipes/{id}")
    public ResponseEntity<List<Recipe>> getRecipesByOwner(@PathVariable String id){
        return ResponseEntity.status(200).body(recipeService.getRecipesByOwner(id));
    }

    @GetMapping("/recipes-without-allergens/{page}")
    public ResponseEntity<List<Recipe>> getRecipePagination(HttpServletRequest request, @PathVariable Integer page){
        String email = String.valueOf(request.getAttribute("username"));
        List<Allergens> allergens = cookUserService.getCookUserByEmail(email).getAllergies();
        return ResponseEntity.status(200).body(recipeService.getRecipesWithoutAlergens(allergens, page, 20));
    }

    @PostMapping("/recipes-with-cuisine-category")
    public ResponseEntity<List<Recipe>> getRecipesWithCuisineAndCategory(@RequestBody CategoryCuisineDto form){
        return ResponseEntity.status(200).body(recipeService.getRecipesWithCategoryAndCuisine(form));
    }

    @GetMapping("/recipes-with-ingredients")
    public ResponseEntity<PaginatedRecipeDto> getRecipesWithIngredients(@RequestParam List<String> ingredients,
                                                                        @RequestParam(defaultValue = "1") Integer page,
                                                                        @RequestParam(defaultValue = "10") Integer pageSize){
        log.info(String.valueOf(ingredients));
        return ResponseEntity.status(200).body(recipeService.getRecipesWithIngredients(ingredients, page, pageSize));
    }

    @GetMapping("/recipe/categories")
    public ResponseEntity<List<RecipeCategory>> getRecipeCategories(){
        return ResponseEntity.status(200).body(recipeService.getRecipeCategories());
    }

    @GetMapping("/recipe/cuisines")
    public ResponseEntity<List<RecipeCuisine>> getRecipeCuisines(){
        return ResponseEntity.status(200).body(recipeService.getRecipeCuisines());
    }

    @GetMapping("/allergens")
    public ResponseEntity<List<Allergens>> getAllergens(){
        return ResponseEntity.status(200).body(recipeService.getAllergens());
    }

    @GetMapping("/recipe/query")
    public ResponseEntity<PaginatedRecipeDto> searchRecipes(@RequestParam String title,
                                                            @RequestParam List<Allergens> allergens,
                                                            @RequestParam List<RecipeCategory> categories,
                                                            @RequestParam List<RecipeCuisine> cuisines,
                                                            @RequestParam(defaultValue = "1") Integer page,
                                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        PaginatedRecipeDto queryRecipes = recipeService.recipeSearch(title,allergens,categories,cuisines,page,pageSize);
        return ResponseEntity.status(200).body(queryRecipes);
    }

    @PutMapping("/recipe")
    public ResponseEntity<?> editRecipe(HttpServletRequest request, @RequestBody Recipe recipe){
        String email = String.valueOf(request.getAttribute("username"));
        Recipe updatedRecipe = recipeService.editRecipe(email, recipe);
        if(updatedRecipe == null){
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(updatedRecipe);
    }
    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(HttpServletRequest request, @PathVariable String id){
        String user_id = String.valueOf(request.getAttribute("username"));
        CookUser cookUser = cookUserService.getCookUserByEmail(user_id);
        Recipe recipe = recipeService.getRecipeById(id);
        if(cookUser != null){
            if(cookUser.getUserRoles().contains(UserRole.ADMIN) || (cookUser.getId().equals(recipe.getOwner()))){
                recipeService.deleteRecipe(id);
                return ResponseEntity.status(200).build();
            }else{
                return ResponseEntity.status(402).build();
            }
        }
        log.info("not present");
        return ResponseEntity.status(403).build();
    }
}
