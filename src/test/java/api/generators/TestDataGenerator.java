package api.generators;

import api.models.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.List;

public class TestDataGenerator {

    private static final String[] VALID_INGREDIENT_IDS = {
            "61c0c5a71d1f82001bdaaa6d",
            "61c0c5a71d1f82001bdaaa6f",
            "61c0c5a71d1f82001bdaaa70",
            "61c0c5a71d1f82001bdaaa71",
            "61c0c5a71d1f82001bdaaa72",
            "61c0c5a71d1f82001bdaaa6e"
    };

    private static final String[] INVALID_INGREDIENT_IDS = {
            "invalid_hash_123",
            "invalid_hash_456",
            "fake_ingredient_789"
    };

    public static User generateRandomUser() {
        return User.builder()
                .email(generateRandomEmail())
                .password(generateRandomPassword())
                .name(generateRandomName())
                .build();
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

    public static List<String> getValidIngredients() {
        return Arrays.asList(
                VALID_INGREDIENT_IDS[0],
                VALID_INGREDIENT_IDS[1],
                VALID_INGREDIENT_IDS[2]
        );
    }

    public static List<String> getSingleValidIngredient() {
        return Arrays.asList(VALID_INGREDIENT_IDS[0]);
    }

    public static List<String> getInvalidIngredients() {
        return Arrays.asList(INVALID_INGREDIENT_IDS);
    }

    public static List<String> getMixedIngredients() {
        return Arrays.asList(
                VALID_INGREDIENT_IDS[0],
                INVALID_INGREDIENT_IDS[0]
        );
    }

    public static User getDefaultUser() {
        return User.builder()
                .email("test-user@test.com")
                .password("test12345")
                .name("TestUser")
                .build();
    }
}