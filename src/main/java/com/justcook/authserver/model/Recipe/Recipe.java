package com.justcook.authserver.model.Recipe;

import com.justcook.authserver.model.Allergens;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class Recipe {
    @Id
    private String id;
    private String title;
    private LocalDateTime creationDate;
    private List<String> ingredients;
    private List<String> steps;
    private String owner;
    private List<Allergens> allergens;
    private List<RecipeCategory> categories;
    private List<RecipeCuisine> cuisines;
    private Integer duration;
    //TODO: OCENY UŻYTKOWNIKÓW
    private String[] images;
    private RecipeDifficulty difficulty;

    public Recipe(String title,
                  List<String> ingredients,
                  List<String> steps,
                  String owner,
                  List<Allergens> allergens,
                  RecipeDifficulty difficulty,
                  String[] images,
                  LocalDateTime creationDate,
                  List<RecipeCategory> categories,
                  List<RecipeCuisine> cuisines,
                  Integer duration
                  ) {
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.owner = owner;
        this.allergens = allergens;
        this.images = images;
        this.difficulty = difficulty;
        this.creationDate = creationDate;
        this.categories = categories;
        this.cuisines = cuisines;
        this.duration = duration;
    }
}
