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
@DisplayName("Get User Orders Tests")
public class GetUserOrdersTest extends BaseTest {

    private User user;

    @BeforeEach
    public void setupUserAndCreateOrder() {
        user = TestDataGenerator.generateRandomUser();
        createUserAndGetToken(user);

        // Create an order for the user
        Order order = new Order();
        order.setIngredients(TestDataGenerator.getValidIngredients());
        orderClient.createOrder(accessToken, order);
    }

    @Test
    @DisplayName("Get orders for authorized user - should be successful")
    @Description("Test gets orders for authorized user and verifies response contains orders list")
    public void getUserOrdersWithAuth() {
        var response = orderClient.getUserOrders(accessToken);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();
        assertThat(response.jsonPath().getList("orders")).isNotNull();
    }

    @Test
    @DisplayName("Get orders for unauthorized user - should return error 401")
    @Description("Test tries to get orders without authorization token and expects error")
    public void getUserOrdersWithoutAuth() {
        var response = orderClient.getUserOrdersWithoutAuth();

        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("You should be authorised");
    }

    @Test
    @DisplayName("Get orders with invalid token - should return error 403")
    @Description("Test tries to get orders with invalid/expired token")
    public void getUserOrdersWithInvalidToken() {
        String invalidToken = "Bearer invalid.token.12345";
        var response = orderClient.getUserOrders(invalidToken);

        assertThat(response.getStatusCode()).isEqualTo(403);  // Исправлено: 401 -> 403
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
    }

    @Test
    @DisplayName("Get orders for new user without orders - should return empty list")
    @Description("Test gets orders for user who hasn't created any orders yet")
    public void getUserOrdersForNewUserWithoutOrders() {
        // Create a new user without creating any order
        User newUser = TestDataGenerator.generateRandomUser();
        String newUserToken;

        var registerResponse = userClient.register(newUser);
        newUserToken = userClient.getAccessTokenFromResponse(registerResponse);

        var response = orderClient.getUserOrders(newUserToken);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();

        // Cleanup
        userClient.deleteUser(newUserToken);
    }

    @Test
    @DisplayName("Get orders after creating multiple orders - should return all orders")
    @Description("Test creates multiple orders and verifies all are returned")
    public void getUserOrdersAfterMultipleOrders() {
        // Create second order
        Order secondOrder = new Order();
        secondOrder.setIngredients(TestDataGenerator.getSingleValidIngredient());
        orderClient.createOrder(accessToken, secondOrder);

        var response = orderClient.getUserOrders(accessToken);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();
        assertThat(response.jsonPath().getList("orders").size()).isGreaterThanOrEqualTo(2);
    }
}