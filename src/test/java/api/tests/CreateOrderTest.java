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
        Order order = Order.builder()
                .ingredients(TestDataGenerator.getValidIngredients())
                .build();

        var response = orderClient.createOrder(accessToken, order);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();
        assertThat(response.jsonPath().getObject("order", Object.class)).isNotNull();
        assertThat(response.jsonPath().getInt("order.number")).isGreaterThan(0);
    }

    @Test
    @DisplayName("Create order without authorization - should be successful (as per docs)")
    @Description("Test creates an order without authorization - according to docs, this should work")
    public void createOrderWithoutAuth() {
        Order order = Order.builder()
                .ingredients(TestDataGenerator.getValidIngredients())
                .build();

        var response = orderClient.createOrderWithoutAuth(order);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();
        assertThat(response.jsonPath().getObject("order", Object.class)).isNotNull();
    }

    @Test
    @DisplayName("Create order with valid ingredients - should be successful")
    @Description("Test creates an order with multiple valid ingredients")
    public void createOrderWithIngredients() {
        Order order = Order.builder()
                .ingredients(TestDataGenerator.getValidIngredients())
                .build();

        var response = orderClient.createOrder(accessToken, order);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();
    }

    @Test
    @DisplayName("Create order with single ingredient - should be successful")
    @Description("Test creates an order with just one ingredient")
    public void createOrderWithSingleIngredient() {
        Order order = Order.builder()
                .ingredients(TestDataGenerator.getSingleValidIngredient())
                .build();

        var response = orderClient.createOrder(accessToken, order);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();
    }

    @Test
    @DisplayName("Create order without ingredients - should return error 400")
    @Description("Test tries to create order without providing any ingredients")
    public void createOrderWithoutIngredients() {
        Order order = Order.builder().ingredients(null).build();

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
        Order order = Order.builder()
                .ingredients(java.util.Collections.emptyList())
                .build();

        var response = orderClient.createOrder(accessToken, order);

        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
    }

    @Test
    @DisplayName("Create order with invalid ingredients hash - should return error 500")
    @Description("Test tries to create order with non-existent ingredient IDs")
    public void createOrderWithInvalidIngredients() {
        Order order = Order.builder()
                .ingredients(TestDataGenerator.getInvalidIngredients())
                .build();

        var response = orderClient.createOrder(accessToken, order);

        assertThat(response.getStatusCode()).isEqualTo(500);
    }

    @Test
    @DisplayName("Create order with mixed valid and invalid ingredients - should return error 500")
    @Description("Test tries to create order with some valid and some invalid ingredient IDs")
    public void createOrderWithMixedIngredients() {
        Order order = Order.builder()
                .ingredients(TestDataGenerator.getMixedIngredients())
                .build();

        var response = orderClient.createOrder(accessToken, order);

        // According to docs, any invalid hash returns 500
        assertThat(response.getStatusCode()).isEqualTo(500);
    }
}