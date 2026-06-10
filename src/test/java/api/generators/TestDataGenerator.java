package api.generators;

import api.models.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.List;

public class TestDataGenerator {

    public static User generateRandomUser() {
        User user = new User();
        user.setEmail(generateRandomEmail());
        user.setPassword(generateRandomPassword());
        user.setName(generateRandomName());
        return user;
    }

    public static String generateRandomEmail() {
        return RandomStringUtils.randomAlphanumeric(10).toLowerCase() + "@test.com";
    }

    public static String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    public static String generateRandomName() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    public static List<String> getInvalidIngredients() {
        return Arrays.asList(
                "invalid_hash_123",
                "invalid_hash_456",
                "fake_ingredient_789"
        );
    }
}