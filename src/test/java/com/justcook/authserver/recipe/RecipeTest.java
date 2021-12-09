package com.justcook.authserver.recipe;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RecipeTest {

    @Test
    public void checkGetRecipesStatusCode() {
        given().
                when().
                get("http://localhost:8080/api/recipes/1").
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void checkGetRecipesStatusCodeV2() {
        //Given
        RestAssured.baseURI ="http://localhost:8080/api/recipes/1";
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
