package com.justcook.authserver.repository;

import com.justcook.authserver.model.Recipe.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    List<Recipe> findRecipesByOwner(String id);
    Recipe findRecipeById(String id);
}
