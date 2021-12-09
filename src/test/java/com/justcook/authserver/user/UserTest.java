package com.justcook.authserver.user;

import com.justcook.authserver.dto.NewUserDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    public void checkRegistrationStatusCode() {
        RestAssured.baseURI ="http://localhost:8080/api";
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
}
