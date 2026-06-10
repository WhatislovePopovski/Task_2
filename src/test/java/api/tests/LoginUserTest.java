package api.tests;

import api.generators.TestDataGenerator;
import api.models.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(AllureJunit5.class)
@DisplayName("Login User Tests")
public class LoginUserTest extends BaseTest {

    private User existingUser;

    @BeforeEach
    public void createExistingUser() {
        existingUser = TestDataGenerator.generateRandomUser();
        var response = userClient.register(existingUser);
        accessToken = userClient.getAccessTokenFromResponse(response);
        createdUser = existingUser;
    }

    @Test
    @DisplayName("Login with existing user - should be successful")
    @Description("Test logs in with correct credentials and verifies successful login")
    public void loginWithExistingUser() {
        var response = userClient.login(existingUser);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();
        assertThat(response.jsonPath().getString("user.email"))
                .isEqualTo(existingUser.getEmail().toLowerCase());
        assertThat(response.jsonPath().getString("user.name"))
                .isEqualTo(existingUser.getName());
        assertThat(response.jsonPath().getString("accessToken")).isNotNull();
        assertThat(response.jsonPath().getString("refreshToken")).isNotNull();
    }

    @Test
    @DisplayName("Login with incorrect email - should return error")
    @Description("Test tries to login with wrong email and expects error 401")
    public void loginWithIncorrectEmail() {
        User userWithWrongEmail = User.builder()
                .email("wrong" + existingUser.getEmail())
                .password(existingUser.getPassword())
                .build();

        var response = userClient.login(userWithWrongEmail);

        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("email or password are incorrect");  // Исправлено
    }

    @Test
    @DisplayName("Login with incorrect password - should return error")
    @Description("Test tries to login with wrong password and expects error 401")
    public void loginWithIncorrectPassword() {
        User userWithWrongPassword = User.builder()
                .email(existingUser.getEmail())
                .password("wrong" + existingUser.getPassword())
                .build();

        var response = userClient.login(userWithWrongPassword);

        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("email or password are incorrect");  // Исправлено
    }

    @Test
    @DisplayName("Login with incorrect email and password - should return error")
    @Description("Test tries to login with completely wrong credentials and expects error 401")
    public void loginWithIncorrectEmailAndPassword() {
        User incorrectUser = User.builder()
                .email("nonexistent@test.com")
                .password("wrongpassword123")
                .build();

        var response = userClient.login(incorrectUser);

        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("email or password are incorrect");  // Исправлено
    }

    @Test
    @DisplayName("Login without email - should return error")
    @Description("Test tries to login without providing email")
    public void loginWithoutEmail() {
        User userWithoutEmail = User.builder()
                .password(existingUser.getPassword())
                .build();

        var response = userClient.login(userWithoutEmail);

        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
    }

    @Test
    @DisplayName("Login without password - should return error")
    @Description("Test tries to login without providing password")
    public void loginWithoutPassword() {
        User userWithoutPassword = User.builder()
                .email(existingUser.getEmail())
                .build();

        var response = userClient.login(userWithoutPassword);

        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
    }
}