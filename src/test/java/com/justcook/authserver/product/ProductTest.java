package com.justcook.authserver.product;

import com.justcook.authserver.model.Product.Product;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class ProductTest {
    private static final String URL = "http://localhost:8080/api";
    private static final String ACCESS_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcnJydXNzc2RiQGdtYWlsLmNvbSIsInJvbGVzIjpbIkFETUlOIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9sb2dpbiIsImV4cCI6MTY0MjA4MjYxNSwidXNlcm5hbWUiOiJBcmVrIn0.85PjHpUcHiSzGomtxMzLHuv7nHGAdDW-rngCQQ-QQGI";

    @Test
    public void checkAddProductStatusCode() {
        RestAssured.baseURI = URL;
        //Given
        RequestSpecification request = RestAssured.given();
        Product product = new Product();
        product.setEan("5610036042912");
        product.setName("Golden chicken");
        //When
        request.body(product);
        request.contentType("application/json");
        request.header("Authorization", ACCESS_TOKEN);

        Response response = request.
                post("/product");
        int statusCode = response.getStatusCode();

        //Then
        Assert.assertEquals("201", String.valueOf(statusCode));
    }

    @Test
    public void checkGetProductByEanStatusCode() {
        given().
                header("Authorization", ACCESS_TOKEN).
                when().
                get(URL+"/product/7610036000429").
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON);
    }
}
