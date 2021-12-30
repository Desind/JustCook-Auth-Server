package com.justcook.authserver.recipe;

import com.justcook.authserver.dto.CategoryCuisineDto;
import com.justcook.authserver.dto.NewRecipeDto;
import com.justcook.authserver.model.Allergens;
import com.justcook.authserver.model.Recipe.RecipeCategory;
import com.justcook.authserver.model.Recipe.RecipeCuisine;
import com.justcook.authserver.model.Recipe.RecipeDifficulty;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class RecipeTest {
    private static final String URL = "http://localhost:8080/api";
    private static final String ACCESS_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJhcnJydXNzc2RiQGdtYWlsLmNvbSIsInJvbGVzIjpbIkFETUlOIl0sImlzcyI6Imh0dHA6Ly" +
            "9sb2NhbGhvc3Q6ODA4MC9sb2dpbiIsImV4cCI6MTY0MDg3MjY2NSwidXNlcm5hbWUiOiJBcmVrIn0.OgfgKH_fOdRkgzI8W0pQEbb1kXVMU7Pi7ZF8bkk_4cY";

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
}
