package com.justcook.authserver.recipe;

import com.justcook.authserver.dto.CategoryCuisineDto;
import com.justcook.authserver.dto.NewRecipeDto;
import com.justcook.authserver.model.Allergens;
import com.justcook.authserver.model.Recipe.Recipe;
import com.justcook.authserver.model.Recipe.RecipeCategory;
import com.justcook.authserver.model.Recipe.RecipeCuisine;
import com.justcook.authserver.model.Recipe.RecipeDifficulty;
import com.justcook.authserver.service.RecipeServiceImpl;
import com.justcook.authserver.service.interfaces.RecipeService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class RecipeTest {
    private static final String URL = "http://localhost:8080/api";
    private static final String ACCESS_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ6YXJhbjE5OThAZ21haWwuY29tIiwicm9sZXMiOlsiQURNSU4iXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2xvZ2luIiwiZXhwIjoxNjQxNDcyODY1LCJ1c2VybmFtZSI6IkRlc2luZCJ9.F3riLLZE-BA01BwnKBWqqWjo9AowoHUl6p23snlOxX8";

    @Test
    public void checkGetRecipesStatusCode() {
        given().
                when().
                get(URL+"/recipes/1").
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void checkGetRecipesByIdStatusCode() {
        given().
                when().
                get(URL+"/recipe/61a4dfb7ff912b0badcb931d").
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void checkGetRecipesByOwnerStatusCode() {
        given().
                header("Authorization", ACCESS_TOKEN).
                when().
                get(URL+"/owner-recipes/61964affddc5b23eb021c7dd").
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void checkGetRecipePaginationStatusCode() {
        given().
                header("Authorization", ACCESS_TOKEN).
                when().
                get(URL+"/recipes-without-allergens/1").
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void checkRegistrationStatusCode() {
        RestAssured.baseURI = URL;
        //Given
        RequestSpecification request = RestAssured.given();
        List<String> testIngredients = new ArrayList<>();
        testIngredients.add("ingr1");

        List<String> testSteps = new ArrayList<>();
        testSteps.add("step1");

        List<Allergens> testAllergens = new ArrayList<>();
        testAllergens.add(Allergens.GLUTEN);

        List<RecipeCategory> testCategories = new ArrayList<>();
        testCategories.add(RecipeCategory.HOLIDAY);

        List<RecipeCuisine> testCuisine = new ArrayList<>();
        testCuisine.add(RecipeCuisine.CHINESE);

        List<String> testImages = new ArrayList<>();
        testSteps.add("img1");

        NewRecipeDto recipe = new NewRecipeDto("test","test",testIngredients,testSteps,testAllergens,
                testCategories,testCuisine,30,testImages, RecipeDifficulty.HARD);
        //When
        request.body(recipe);
        request.header("Authorization", ACCESS_TOKEN);
        request.contentType("application/json");

        Response response = request.
                post("/recipe");
        int statusCode = response.getStatusCode();

        //Then
        Assert.assertEquals("201", String.valueOf(statusCode));
    }

    @Test
    public void checkRecipesWithCuisineAndCategoryStatusCode() {
        RestAssured.baseURI = URL;
        //Given
        RequestSpecification request = RestAssured.given();
        List<RecipeCuisine> cuisines = new ArrayList<>();
        cuisines.add(RecipeCuisine.POLISH);

        List<RecipeCategory> categories = new ArrayList<>();
        categories.add(RecipeCategory.SOUP);

        CategoryCuisineDto categoryCuisine = new CategoryCuisineDto(cuisines,categories);
        request.body(categoryCuisine);
        request.header("Authorization", ACCESS_TOKEN);
        request.contentType("application/json");

        Response response = request.
                post("/recipes-with-cuisine-category");
        int statusCode = response.getStatusCode();

        //Then
        Assert.assertEquals("200", String.valueOf(statusCode));
    }


    @Test
    public void checkGetRecipesStatusCodeV2() {
        //Given
        RestAssured.baseURI = URL+"/recipes/1";
        RequestSpecification request = RestAssured.given();
        request.contentType("application/json");

        //When
        Response response = request.
                get();
        int statusCode = response.getStatusCode();

        //Then
        Assert.assertEquals("200", String.valueOf(statusCode));
    }

    @ParameterizedTest
    @CsvSource({
            "ingredient,ingredient2,true",
            "ingredient,INGREDIENT,true",
            "ingredients1,INGREDIENTS2,false",
            ",,false"
    })
    public void checkGetRecipesWithIngredients(String ingredient1, String ingredient2, boolean expectedResult){
        //Given
        NewRecipeDto newRecipeDto = new NewRecipeDto("search test title",
                "search test description",
                List.of("ingredient","ingredient2"),
                List.of("step1","step2"),
                List.of(Allergens.LUPINE),
                List.of(RecipeCategory.DINNER),
                List.of(RecipeCuisine.CHINESE),
                15,
                List.of("testimg"),
                RecipeDifficulty.HARD);

        //ADD RECIPE
        RequestSpecification request = RestAssured.given();
        request.body(newRecipeDto);
        request.header("Authorization", ACCESS_TOKEN);
        request.contentType("application/json");
        Recipe resultRecipe = request.post(URL + "/recipe").then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(Recipe.class);


        //SEARCH
        RequestSpecification searchRequest = RestAssured.given();
        searchRequest.contentType("application/json");
        String searchedRecipes = get(URL + "/recipes-with-ingredients?ingredients=" + ingredient1 + "," + ingredient2).then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
        if(expectedResult){
            Assert.assertTrue(searchedRecipes.contains(resultRecipe.getId()));
        }else{
            Assert.assertFalse(searchedRecipes.contains(resultRecipe.getId()));
        }

        //CLEAR
        RequestSpecification clearRequest = RestAssured.given();
        clearRequest.header("Authorization", ACCESS_TOKEN);
        clearRequest.contentType("application/json");
        clearRequest.delete(URL + "/recipe/" + resultRecipe.getId()).then()
                .assertThat()
                .statusCode(200);

    }
}
