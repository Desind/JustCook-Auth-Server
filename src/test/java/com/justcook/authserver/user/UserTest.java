package com.justcook.authserver.user;

import com.justcook.authserver.dto.NewUserDto;
import com.justcook.authserver.dto.RoleToUserDto;
import com.justcook.authserver.model.User.UserRole;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class UserTest {
    private static final String URL = "http://localhost:8080/api";
    private static final String ACCESS_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcnJydXNzc2RiQGdtYWlsLmNvbSIsInJvbGVzIjpbIkFETUlOIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9sb2dpbiIsImV4cCI6MTY0MjA4MjYxNSwidXNlcm5hbWUiOiJBcmVrIn0.85PjHpUcHiSzGomtxMzLHuv7nHGAdDW-rngCQQ-QQGI";
    private static final String REFRESH_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcnJydXNzc2RiQGdtYWlsLmNvbSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9sb2dpbiJ9.tk8PTncAJ7jk4tSI43-LcDsfjoKrC18maiy7vvpDlek";


    @Test
    public void checkRegistration() {
        RestAssured.baseURI = URL;
        //Given
        RequestSpecification request = RestAssured.given();

        //When
        NewUserDto user = new NewUserDto();
        user.setUsername("Gienek");
        user.setEmail("g@gmail.com");
        user.setPassword("gienek123");
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
    public void likeRecipe() {
        given().
                header("Authorization", ACCESS_TOKEN).
                when().
                put(URL+"/like-recipe/61a745d181410247d1451b0a").
                then().
                assertThat().
                statusCode(200);
    }

    @Test
    public void dislikeRecipe() {
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
