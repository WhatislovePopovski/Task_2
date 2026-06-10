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
@DisplayName("Update User Tests")
public class UpdateUserTest extends BaseTest {

    private User originalUser;

    @BeforeEach
    public void setupUser() {
        originalUser = TestDataGenerator.generateRandomUser();
        createUserAndGetToken(originalUser);
    }

    @Test
    @DisplayName("Update user email with authorization - should be successful")
    @Description("Test updates user's email when authorized and verifies the change")
    public void updateUserEmailWithAuth() {
        String newEmail = TestDataGenerator.generateRandomEmail();

        // Передаем полные данные пользователя
        User updatedUser = new User();
        updatedUser.setEmail(newEmail);
        updatedUser.setPassword(originalUser.getPassword());
        updatedUser.setName(originalUser.getName());

        var response = userClient.updateUser(accessToken, updatedUser);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();
        assertThat(response.jsonPath().getString("user.email")).isEqualTo(newEmail.toLowerCase());
        assertThat(response.jsonPath().getString("user.name")).isEqualTo(originalUser.getName());
    }

    @Test
    @DisplayName("Update user name with authorization - should be successful")
    @Description("Test updates user's name when authorized and verifies the change")
    public void updateUserNameWithAuth() {
        String newName = TestDataGenerator.generateRandomName();

        // Передаем полные данные пользователя с новым именем
        User updatedUser = new User();
        updatedUser.setEmail(originalUser.getEmail());
        updatedUser.setPassword(originalUser.getPassword());
        updatedUser.setName(newName);

        var response = userClient.updateUser(accessToken, updatedUser);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();
        assertThat(response.jsonPath().getString("user.name")).isEqualTo(newName);
        assertThat(response.jsonPath().getString("user.email"))
                .isEqualTo(originalUser.getEmail().toLowerCase());
    }

    @Test
    @DisplayName("Update both email and name with authorization - should be successful")
    @Description("Test updates both email and name when authorized and verifies the changes")
    public void updateBothFieldsWithAuth() {
        String newEmail = TestDataGenerator.generateRandomEmail();
        String newName = TestDataGenerator.generateRandomName();

        User newUserData = new User();
        newUserData.setEmail(newEmail);
        newUserData.setName(newName);
        newUserData.setPassword(originalUser.getPassword());

        var response = userClient.updateUser(accessToken, newUserData);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("success")).isTrue();
        assertThat(response.jsonPath().getString("user.email"))
                .isEqualTo(newEmail.toLowerCase());
        assertThat(response.jsonPath().getString("user.name"))
                .isEqualTo(newName);
    }

    @Test
    @DisplayName("Update user without authorization - should return error 401")
    @Description("Test tries to update user data without access token and expects error")
    public void updateUserWithoutAuth_Email() {
        String newEmail = TestDataGenerator.generateRandomEmail();
        User updatedUser = new User();
        updatedUser.setEmail(newEmail);

        var response = userClient.updateUserWithoutAuth(updatedUser);

        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("You should be authorised");
    }

    @Test
    @DisplayName("Update user name without authorization - should return error 401")
    @Description("Test tries to update user name without access token and expects error")
    public void updateUserWithoutAuth_Name() {
        String newName = TestDataGenerator.generateRandomName();
        User updatedUser = new User();
        updatedUser.setName(newName);

        var response = userClient.updateUserWithoutAuth(updatedUser);

        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("You should be authorised");
    }

    @Test
    @DisplayName("Update user with invalid token - should return error 403")
    @Description("Test tries to update user with invalid/expired token")
    public void updateUserWithInvalidToken() {
        String newEmail = TestDataGenerator.generateRandomEmail();
        User updatedUser = new User();
        updatedUser.setEmail(newEmail);
        String invalidToken = "Bearer invalid.token.12345";

        var response = userClient.updateUser(invalidToken, updatedUser);

        assertThat(response.getStatusCode()).isEqualTo(403);
        assertThat(response.jsonPath().getBoolean("success")).isFalse();
    }
}