package com.justcook.authserver.service;

import com.justcook.authserver.dto.CategoryCuisineDto;
import com.justcook.authserver.dto.NewRecipeDto;
import com.justcook.authserver.dto.PaginatedRecipeDto;
import com.justcook.authserver.dto.RecipeDto;
import com.justcook.authserver.model.Allergens;
import com.justcook.authserver.model.Recipe.Recipe;
import com.justcook.authserver.model.Recipe.RecipeCategory;
import com.justcook.authserver.model.Recipe.RecipeCuisine;
import com.justcook.authserver.model.Recipe.RecipeDifficulty;
import com.justcook.authserver.model.User.CookUser;
import com.justcook.authserver.repository.CookUserRepository;
import com.justcook.authserver.repository.RecipeRepository;
import com.justcook.authserver.service.interfaces.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final CookUserRepository cookUserRepository;

    public List<Recipe> getAllRecipes(int page, int records) {
        return recipeRepository.findAllByOrderByCreationDateDesc().stream().skip((page-1)*records).limit(records).collect(Collectors.toList());
    }

    @Override
    public Recipe createNewRecipe(NewRecipeDto newRecipeDto, String owner){
        if(newRecipeDto.getTitle() == null || newRecipeDto.getIngredients() == null || newRecipeDto.getSteps() == null || owner == null){
            return null;
        }
        RecipeDifficulty recipeDifficulty = RecipeDifficulty.AVERAGE;
        if(newRecipeDto.getDifficulty() != null){
            recipeDifficulty = newRecipeDto.getDifficulty();
        }

        Recipe recipe = new Recipe(
                newRecipeDto.getTitle(),
                newRecipeDto.getDescription(),
                newRecipeDto.getIngredients(),
                newRecipeDto.getSteps(),
                owner,
                newRecipeDto.getAllergens(),
                recipeDifficulty,
                newRecipeDto.getImages(),
                LocalDateTime.now(),
                newRecipeDto.getCategories(),
                newRecipeDto.getCuisines(),
                newRecipeDto.getDuration()

        );
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
    public PaginatedRecipeDto getRecipesWithIngredients(List<String> ingredients, Integer page, Integer pageSize) {
        //TODO: CHANGE THIS ABOMINATION
        String newRegex = "^";
        for (String ingredient : ingredients) {
            newRegex += "(?=.*\\b" + ingredient.toUpperCase() + "\\b)";
        }
        newRegex += ".*$";
        System.out.println(newRegex);
        List<Recipe> recipes = recipeRepository.findAll();
        List<Recipe> recipesWithIngredients = new ArrayList<>();
        for(Recipe recipe : recipes){
            if(recipe.getIngredients().toString().toUpperCase().matches(newRegex)){
                recipesWithIngredients.add(recipe);
            }
        }
        Integer pagesCount = (int) Math.ceil(recipesWithIngredients.size()/(double)pageSize);
        if(page>pagesCount){
            page = pagesCount;
        }
        if(page<1){
            pagesCount=1;
            page=1;
        }
        recipesWithIngredients.sort(Comparator.comparing(Recipe::getCreationDate).reversed());
        PaginatedRecipeDto paginatedRecipeDto =  new PaginatedRecipeDto(page,pageSize,pagesCount,recipesWithIngredients.stream().skip((page-1)*pageSize).limit(pageSize).toList());
        return paginatedRecipeDto;
    }

    @Override
    public List<RecipeCategory> getRecipeCategories() {
        return Arrays.asList(RecipeCategory.values());
    }

    @Override
    public List<RecipeCuisine> getRecipeCuisines() {
        return Arrays.asList(RecipeCuisine.values());
    }

    @Override
    public List<Allergens> getAllergens() {
        return Arrays.asList(Allergens.values());
    }

    @Override
    public List<RecipeDto> getAdminRecipes(String title, String owner) {
        Optional<CookUser> user = Optional.ofNullable(cookUserRepository.findByEmail(owner));
        List<RecipeDto> recipes;
        if(user.isPresent()){
            recipes = recipeRepository.findRecipesByTitleContainsAndOwnerIsLike(title,user.get().getId());
        }else{
            recipes = recipeRepository.findRecipesByTitleContainsAndOwnerIsLike(title,"");
        }
        for(RecipeDto recipe : recipes){
            Optional<CookUser> u = cookUserRepository.findById(recipe.getOwner());
            u.ifPresent(cookUser -> recipe.setOwnerUsername(cookUser.getUsername()));
        }
        return recipes;
    }

    @Override
    public void deleteRecipe(String id) {
        recipeRepository.deleteById(id);
    }

    public PaginatedRecipeDto recipeSearch(String title, List<Allergens> allergens, List<RecipeCategory> categories, List<RecipeCuisine> cuisines, Integer page, Integer pageSize){
        if(categories.isEmpty()){
            categories = Arrays.asList(RecipeCategory.values());
        }
        if(cuisines.isEmpty()){
            cuisines = Arrays.asList(RecipeCuisine.values());
        }
        List<Recipe> recipeList = recipeRepository.queryRecipes(title,allergens,categories,cuisines);
        recipeList.sort(Comparator.comparing(Recipe::getCreationDate).reversed());
        Integer pagesCount = (int) Math.ceil(recipeList.size()/(double)pageSize);
        if(page>pagesCount){
            page = pagesCount;
        }if(page<1){
            pagesCount=1;
            page=1;
        }
        return new PaginatedRecipeDto(
                page,
                pageSize,
                pagesCount,
                recipeList.stream().skip((page-1)*pageSize).limit(pageSize).toList());
    }

    public Recipe editRecipe(String email, Recipe recipe){
        String userId = cookUserRepository.findByEmail(email).getId();
        if(userId.equals(recipe.getOwner()) && recipeRepository.existsById(recipe.getId())){
            return recipeRepository.save(recipe);
        }
        return null;
    }
}
