package com.justcook.authserver.model.Recipe;

import com.justcook.authserver.model.Allergens;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Recipe {
    @Id
    private String id;
    private String title;
    private List<String> ingredients;
    private List<String> steps;
    private String owner;
    private List<Allergens> allergens;
    private RecipeDifficulty recipeDifficulty;

    public Recipe(String title, List<String> ingredients, List<String> steps, String owner, List<Allergens> allergens, RecipeDifficulty recipeDifficulty) {
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.owner = owner;
        this.allergens = allergens;
        this.recipeDifficulty = recipeDifficulty;
    }
}
