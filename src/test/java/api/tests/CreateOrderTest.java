package api.tests;

import api.generators.TestDataGenerator;
import api.models.Order;
import api.models.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(AllureJunit5.class)
@DisplayName("Create Order Tests")
public class CreateOrderTest extends BaseTest {

    private User user;

    @BeforeEach
    public void setupUser() {
        user = TestDataGenerator.generateRandomUser();
        createUserAndGetToken(user);
    }

    @Test
    @DisplayName("Create order with authorization - should be successful")
    @Description("Test creates an order with valid ingredients when authorized")
    public void createOrderWithAuth() {
        // Пропускаем тест, если нет ингредиентов
        if (validIngredientIds.isEmpty()) {
            System.err.println("No ingredients available, skipping test");
            return;
        }

        List<String> ingredients = validIngredientIds.subList(0, Math.min(3, validIngredientIds.size()));

        Order order = new Order();
        order.setIngredients(ingredients);

        var response = orderClient.createOrder(accessToken, order);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();
        assertThat(response.jsonPath().getObject("order", Object.class)).isNotNull();
        assertThat(response.jsonPath().getInt("order.number")).isGreaterThan(0);
    }

    @Test
    @DisplayName("Create order without authorization - should be successful")
    @Description("Test creates an order without authorization")
    public void createOrderWithoutAuth() {
        if (validIngredientIds.isEmpty()) {
            System.err.println("No ingredients available, skipping test");
            return;
        }

        List<String> ingredients = validIngredientIds.subList(0, Math.min(3, validIngredientIds.size()));

        Order order = new Order();
        order.setIngredients(ingredients);

        var response = orderClient.createOrderWithoutAuth(order);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();
        assertThat(response.jsonPath().getObject("order", Object.class)).isNotNull();
    }

    @Test
    @DisplayName("Create order with valid ingredients - should be successful")
    @Description("Test creates an order with multiple valid ingredients")
    public void createOrderWithIngredients() {
        if (validIngredientIds.isEmpty()) {
            System.err.println("No ingredients available, skipping test");
            return;
        }

        List<String> ingredients = validIngredientIds.subList(0, Math.min(3, validIngredientIds.size()));

        Order order = new Order();
        order.setIngredients(ingredients);

        var response = orderClient.createOrder(accessToken, order);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();
    }

    @Test
    @DisplayName("Create order with single ingredient - should be successful")
    @Description("Test creates an order with just one ingredient")
    public void createOrderWithSingleIngredient() {
        if (validIngredientIds.isEmpty()) {
            System.err.println("No ingredients available, skipping test");
            return;
        }

        List<String> ingredients = Arrays.asList(validIngredientIds.get(0));

        Order order = new Order();
        order.setIngredients(ingredients);

        var response = orderClient.createOrder(accessToken, order);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();
    }

    @Test
    @DisplayName("Create order without ingredients - should return error 400")
    @Description("Test tries to create order without providing any ingredients")
    public void createOrderWithoutIngredients() {
        Order order = new Order();
        order.setIngredients(null);

        var response = orderClient.createOrder(accessToken, order);

        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Create order with empty ingredients list - should return error 400")
    @Description("Test tries to create order with empty ingredients array")
    public void createOrderWithEmptyIngredients() {
        Order order = new Order();
        order.setIngredients(Collections.emptyList());

        var response = orderClient.createOrder(accessToken, order);

        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
    }

    @Test
    @DisplayName("Create order with invalid ingredients hash - should return error 500")
    @Description("Test tries to create order with non-existent ingredient IDs")
    public void createOrderWithInvalidIngredients() {
        List<String> invalidIngredients = TestDataGenerator.getInvalidIngredients();

        Order order = new Order();
        order.setIngredients(invalidIngredients);

        var response = orderClient.createOrder(accessToken, order);

        assertThat(response.getStatusCode()).isEqualTo(500);
    }

    @Test
    @DisplayName("Create order with mixed valid and invalid ingredients - should return error 500")
    @Description("Test tries to create order with some valid and some invalid ingredient IDs")
    public void createOrderWithMixedIngredients() {
        if (validIngredientIds.isEmpty()) {
            System.err.println("No ingredients available, skipping test");
            return;
        }

        List<String> mixedIngredients = Arrays.asList(
                validIngredientIds.get(0),
                "invalid_hash_123"
        );

        Order order = new Order();
        order.setIngredients(mixedIngredients);

        var response = orderClient.createOrder(accessToken, order);

        assertThat(response.getStatusCode()).isEqualTo(500);
    }
}