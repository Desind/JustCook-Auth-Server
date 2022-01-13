package com.justcook.authserver.user;

import com.justcook.authserver.dto.NewUserDto;
import com.justcook.authserver.dto.RoleToUserDto;
import com.justcook.authserver.model.Recipe.Recipe;
import com.justcook.authserver.model.User.UserRole;
import com.justcook.authserver.service.CookUserServiceImpl;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;

public class UserTest {
    private static final String URL = "http://localhost:8080/api";
    private static final String ACCESS_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZXNpbmQiLCJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXBpL3JlZnJlc2gtdG9rZW4iLCJleHAiOjE2NDE0ODI3MDJ9.BgQnPBprWcJwH4l5txIvWF_iPTvePJzR8MllcOgpeLM";
    private static final String REFRESH_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ6YXJhbjE5OThAZ21haWwuY29tIiwicm9sZXMiOlsiQURNSU4iXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2xvZ2luIiwiZXhwIjoxNjQxNDcyODY1LCJ1c2VybmFtZSI6IkRlc2luZCJ9.F3riLLZE-BA01BwnKBWqqWjo9AowoHUl6p23snlOxX8";


    @Test
    public void checkRegistrationStatusCode() {
        RestAssured.baseURI = URL;
        //Given
        RequestSpecification request = RestAssured.given();

        //When !user has to be unique - should not exist in the DB
//        JSONObject requestParams = new JSONObject();
//        requestParams.put("username", "Gienek");
//        requestParams.put("email", "g@gmail.com");
//        requestParams.put("password", "gienek123");
        NewUserDto user = new NewUserDto();
        user.setUsername("Gienek");
        user.setEmail("g@gmail.com");
        user.setPassword("gienek123");
//        request.body(requestParams.toJSONString());
        request.body(user);
        request.contentType("application/json");

        Response response = request.
                post("/user");
        int statusCode = response.getStatusCode();

        //Then
        Assert.assertEquals("201", String.valueOf(statusCode));
    }

    @Test
    public void checkGetAllUsers() {
        //Given
        RestAssured.baseURI ="http://localhost:8080/api/users";
        //authorization header has to be fresh
        RequestSpecification request = RestAssured.given().header("Authorization", ACCESS_TOKEN);
        request.contentType("application/json");

        //When
        Response response = request.
                get();
        int statusCode = response.getStatusCode();

        //Then
        Assert.assertEquals("200", String.valueOf(statusCode));
    }

    @Test
    public void likeRecipeStatusCode() {
        given().
                header("Authorization", ACCESS_TOKEN).
                when().
                put(URL+"/like-recipe/61a745d181410247d1451b0a").
                then().
                assertThat().
                statusCode(200);
    }

    @Test
    public void dislikeRecipeStatusCode() {
        given().
                header("Authorization", ACCESS_TOKEN).
                when().
                delete(URL+"/dislike-recipe/61a745d181410247d1451b0a").
                then().
                assertThat().
                statusCode(200);
    }

    @Test
    public void checkRefreshToken(){
        JWT jwt =  given().header("Authorization", REFRESH_TOKEN)
                .when()
                .get(URL+"/refresh-token")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(JWT.class);

        given().header("Authorization",jwt.getToken_type() + jwt.getAccess_token())
                .when()
                .get(URL + "/permission-check")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void checkGiveUserRole(){
        RoleToUserDto roleToUserDto = new RoleToUserDto("g@gmail.com", UserRole.ADMIN.name());

        RequestSpecification request = RestAssured.given();
        request.body(roleToUserDto);
        request.header("Authorization", ACCESS_TOKEN);
        request.contentType("application/json");
        request.post(URL + "/user-role").then()
                .assertThat()
                .statusCode(200);

        roleToUserDto.setUserRole(UserRole.USER.name());
        request.body(roleToUserDto);
        request.post(URL + "/user-role").then()
                .assertThat()
                .statusCode(200);
    }
}
