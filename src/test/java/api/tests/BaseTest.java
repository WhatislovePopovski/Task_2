package api.tests;

import api.clients.IngredientsClient;
import api.clients.OrderClient;
import api.clients.UserClient;
import api.models.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseTest {
    protected UserClient userClient;
    protected OrderClient orderClient;
    protected IngredientsClient ingredientsClient;
    protected User createdUser;
    protected String accessToken;
    protected String refreshToken;
    protected List<String> validIngredientIds;

    @BeforeEach
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        ingredientsClient = new IngredientsClient();

        // Получаем валидные ингредиенты из API перед каждым тестом
        try {
            validIngredientIds = ingredientsClient.getAllIngredientIds();
            System.out.println("Loaded " + validIngredientIds.size() + " valid ingredients");
        } catch (Exception e) {
            System.err.println("Failed to load ingredients: " + e.getMessage());
            // Если не удалось загрузить ингредиенты, используем заглушку для некоторых тестов
            validIngredientIds = List.of();
        }
    }

    @AfterEach
    public void cleanUp() {
        if (accessToken != null && createdUser != null) {
            try {
                userClient.deleteUser(accessToken);
                System.out.println("User deleted successfully");
            } catch (Exception e) {
                System.out.println("Cleanup failed: " + e.getMessage());
            }
        }
    }

    @Step("Create user and get token")
    protected void createUserAndGetToken(User user) {
        createdUser = user;
        Response response = userClient.register(user);

        System.out.println("Register response status: " + response.getStatusCode());
        System.out.println("Register response body: " + response.getBody().asString());

        assertThat(response.getStatusCode()).isEqualTo(200);

        accessToken = userClient.getAccessTokenFromResponse(response);
        refreshToken = userClient.getRefreshTokenFromResponse(response);

        System.out.println("Access token: " + accessToken);
        System.out.println("Refresh token: " + refreshToken);
    }

    @Step("Login user and get token")
    protected void loginUserAndGetToken(User user) {
        Response response = userClient.login(user);
        assertThat(response.getStatusCode()).isEqualTo(200);

        accessToken = userClient.getAccessTokenFromResponse(response);
        refreshToken = userClient.getRefreshTokenFromResponse(response);
    }

    protected void deleteUser() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}