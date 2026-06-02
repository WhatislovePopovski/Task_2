package api.clients;

import api.config.ApiConfig;
import api.models.Order;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {

    public Response createOrder(String accessToken, Order order) {
        return given()
                .spec(ApiConfig.getRequestSpec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ApiConfig.ORDERS_ENDPOINT);
    }

    public Response createOrderWithoutAuth(Order order) {
        return given()
                .spec(ApiConfig.getRequestSpec())
                .body(order)
                .when()
                .post(ApiConfig.ORDERS_ENDPOINT);
    }

    public Response getUserOrders(String accessToken) {
        return given()
                .spec(ApiConfig.getRequestSpec())
                .header("Authorization", accessToken)
                .when()
                .get(ApiConfig.ORDERS_ENDPOINT);
    }

    public Response getUserOrdersWithoutAuth() {
        return given()
                .spec(ApiConfig.getRequestSpec())
                .when()
                .get(ApiConfig.ORDERS_ENDPOINT);
    }

    public Response getAllOrders() {
        return given()
                .spec(ApiConfig.getRequestSpec())
                .when()
                .get(ApiConfig.ALL_ORDERS_ENDPOINT);
    }
}