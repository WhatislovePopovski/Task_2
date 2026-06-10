package api.clients;

import api.config.ApiConfig;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class IngredientsClient {

    public Response getIngredients() {
        return given()
                .spec(ApiConfig.getRequestSpec())
                .when()
                .get(ApiConfig.INGREDIENTS_ENDPOINT);
    }

    // Метод для получения списка ID всех ингредиентов
    public List<String> getAllIngredientIds() {
        Response response = getIngredients();
        if (response.getStatusCode() != 200) {
            System.err.println("Failed to get ingredients. Status code: " + response.getStatusCode());
            return new ArrayList<>();
        }
        return response.jsonPath().getList("data._id");
    }

    // Метод для получения нескольких случайных ингредиентов
    public List<String> getRandomValidIngredients(int count) {
        List<String> allIds = getAllIngredientIds();
        if (allIds.isEmpty()) {
            return new ArrayList<>();
        }
        if (allIds.size() < count) {
            return allIds;
        }
        // Берем первые count ингредиентов
        return allIds.subList(0, Math.min(count, allIds.size()));
    }

    // Получить один валидный ингредиент
    public String getValidIngredientId() {
        List<String> allIds = getAllIngredientIds();
        if (allIds.isEmpty()) {
            return null;
        }
        return allIds.get(0);
    }
}