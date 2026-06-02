package api.tests;

import api.generators.TestDataGenerator;
import api.models.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(AllureJunit5.class)
@DisplayName("Create User Tests")
public class CreateUserTest extends BaseTest {

    @Test
    @DisplayName("Create unique user - should be successful")
    @Description("Test creates a new unique user and verifies successful registration")
    public void createUniqueUser() {
        User uniqueUser = TestDataGenerator.generateRandomUser();

        var response = userClient.register(uniqueUser);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();
        assertThat(response.jsonPath().getString("user.email")).isEqualTo(uniqueUser.getEmail().toLowerCase());
        assertThat(response.jsonPath().getString("user.name")).isEqualTo(uniqueUser.getName());
        assertThat(response.jsonPath().getString("accessToken")).isNotNull();
        assertThat(response.jsonPath().getString("refreshToken")).isNotNull();

        // Save for cleanup
        createdUser = uniqueUser;
        accessToken = userClient.getAccessTokenFromResponse(response);
    }

    @Test
    @DisplayName("Create already registered user - should return error")
    @Description("Test tries to create a user that already exists and expects error 403")
    public void createAlreadyRegisteredUser() {
        User user = TestDataGenerator.generateRandomUser();
        userClient.register(user);

        var response = userClient.register(user);

        assertThat(response.getStatusCode()).isEqualTo(403);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("User already exists");

        // Save for cleanup
        createdUser = user;
        var loginResponse = userClient.login(user);
        accessToken = userClient.getAccessTokenFromResponse(loginResponse);
    }

    @Test
    @DisplayName("Create user without email - should return error")
    @Description("Test creates user without email field and expects error about required fields")
    public void createUserWithoutEmail() {
        User userWithPasswordAndName = User.builder()
                .password(TestDataGenerator.generateRandomPassword())
                .name(TestDataGenerator.generateRandomName())
                .build();

        var response = userClient.register(userWithPasswordAndName);

        assertThat(response.getStatusCode()).isEqualTo(403);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("Email, password and name are required fields");
    }

    @Test
    @DisplayName("Create user without password - should return error")
    @Description("Test creates user without password field and expects error about required fields")
    public void createUserWithoutPassword() {
        User userWithEmailAndName = User.builder()
                .email(TestDataGenerator.generateRandomEmail())
                .name(TestDataGenerator.generateRandomName())
                .build();

        var response = userClient.register(userWithEmailAndName);

        assertThat(response.getStatusCode()).isEqualTo(403);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("Email, password and name are required fields");
    }

    @Test
    @DisplayName("Create user without name - should return error")
    @Description("Test creates user without name field and expects error about required fields")
    public void createUserWithoutName() {
        User userWithEmailAndPassword = User.builder()
                .email(TestDataGenerator.generateRandomEmail())
                .password(TestDataGenerator.generateRandomPassword())
                .build();

        var response = userClient.register(userWithEmailAndPassword);

        assertThat(response.getStatusCode()).isEqualTo(403);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("Email, password and name are required fields");
    }

    @Test
    @DisplayName("Create user with empty strings - should return error")
    @Description("Test creates user with empty email and password")
    public void createUserWithEmptyFields() {
        User emptyUser = User.builder()
                .email("")
                .password("")
                .name("")
                .build();

        var response = userClient.register(emptyUser);

        assertThat(response.getStatusCode()).isEqualTo(403);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
    }
}