package api.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.qameta.allure.restassured.AllureRestAssured;

import static io.restassured.http.ContentType.JSON;

public class ApiConfig {
    public static final String BASE_URL = "https://stellarburgers.education-services.ru";

    // Endpoints
    public static final String REGISTER_ENDPOINT = "/api/auth/register";
    public static final String LOGIN_ENDPOINT = "/api/auth/login";
    public static final String USER_ENDPOINT = "/api/auth/user";
    public static final String LOGOUT_ENDPOINT = "/api/auth/logout";
    public static final String TOKEN_ENDPOINT = "/api/auth/token";
    public static final String ORDERS_ENDPOINT = "/api/orders";
    public static final String ALL_ORDERS_ENDPOINT = "/api/orders/all";
    public static final String INGREDIENTS_ENDPOINT = "/api/ingredients";
    public static final String PASSWORD_RESET_ENDPOINT = "/api/password-reset";
    public static final String PASSWORD_RESET_RESET_ENDPOINT = "/api/password-reset/reset";

    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setContentType(JSON)
                .setBaseUri(BASE_URL)
                .addFilter(new AllureRestAssured())
                .build();
    }
}