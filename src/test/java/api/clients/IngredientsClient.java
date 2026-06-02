package api.clients;

import api.config.ApiConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientsClient {

    public Response getIngredients() {
        return given()
                .spec(ApiConfig.getRequestSpec())
                .when()
                .get(ApiConfig.INGREDIENTS_ENDPOINT);
    }
}