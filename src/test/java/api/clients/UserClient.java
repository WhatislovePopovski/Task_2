package api.clients;

import api.config.ApiConfig;
import api.models.User;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {

    public Response register(User user) {
        return given()
                .spec(ApiConfig.getRequestSpec())
                .body(user)
                .when()
                .post(ApiConfig.REGISTER_ENDPOINT);
    }

    public Response login(User user) {
        return given()
                .spec(ApiConfig.getRequestSpec())
                .body(user)
                .when()
                .post(ApiConfig.LOGIN_ENDPOINT);
    }

    public Response updateUser(String accessToken, User user) {
        System.out.println("Updating user with token: " + accessToken);
        System.out.println("Update data: " + user.getEmail() + ", " + user.getName());

        return given()
                .spec(ApiConfig.getRequestSpec())
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(ApiConfig.USER_ENDPOINT);
    }

    public Response updateUserWithoutAuth(User user) {
        return given()
                .spec(ApiConfig.getRequestSpec())
                .body(user)
                .when()
                .patch(ApiConfig.USER_ENDPOINT);
    }

    public Response getUser(String accessToken) {
        return given()
                .spec(ApiConfig.getRequestSpec())
                .header("Authorization", accessToken)
                .when()
                .get(ApiConfig.USER_ENDPOINT);
    }

    public Response deleteUser(String accessToken) {
        return given()
                .spec(ApiConfig.getRequestSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(ApiConfig.USER_ENDPOINT);
    }

    public Response logout(String refreshToken) {
        return given()
                .spec(ApiConfig.getRequestSpec())
                .body("{\"token\": \"" + refreshToken + "\"}")
                .when()
                .post(ApiConfig.LOGOUT_ENDPOINT);
    }

    public String getAccessTokenFromResponse(Response response) {
        String token = response.jsonPath().getString("accessToken");
        System.out.println("Raw token from response: " + token);

        if (token == null || token.isEmpty()) {
            return null;
        }
        if (!token.startsWith("Bearer ")) {
            return "Bearer " + token;
        }
        return token;
    }

    public String getRefreshTokenFromResponse(Response response) {
        return response.jsonPath().getString("refreshToken");
    }
}