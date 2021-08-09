package com.justcook.authserver.rest;

import com.justcook.authserver.model.Recipe.Recipe;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/recipe")
public class RecipeController {

    public RecipeController() {

    }
}
