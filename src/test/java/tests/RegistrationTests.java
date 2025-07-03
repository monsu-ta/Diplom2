package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.Test;
import page.AuthPage;

import static org.hamcrest.Matchers.equalTo;

public class RegistrationTests extends BaseTest {
    private AuthPage authPage = new AuthPage();

    @Test
    @DisplayName("Успешная регистрация")
    @Description("Проверка регистрации нового пользователя")
    public void testSuccessfulRegistration() {
        User newUser = new User(
                "test" + System.currentTimeMillis() + "@example.com",
                "password123",
                "Test User"
        );

        authPage.register(newUser)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Регистрация без email")
    @Description("Проверка ошибки при регистрации без указания email")
    public void testRegistrationWithoutEmail() {
        User userWithoutEmail = new User(null, "password123", "Test User");

        authPage.register(userWithoutEmail)
                .then()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация без пароля")
    @Description("Проверка ошибки при регистрации без указания пароля")
    public void testRegistrationWithoutPassword() {
        User userWithoutPassword = new User("test@example.com", null, "Test User");

        authPage.register(userWithoutPassword)
                .then()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Повторная регистрация существующего пользователя")
    @Description("Проверка ошибки при повторной регистрации пользователя")
    public void testRegisterExistingUser() {
        authPage.register(testUser)
                .then()
                .statusCode(403)
                .body("message", equalTo("User already exists"));
    }
}