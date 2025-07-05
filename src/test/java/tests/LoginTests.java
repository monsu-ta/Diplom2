package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.Test;
import page.AuthPage;

import static org.hamcrest.Matchers.equalTo;

public class LoginTests extends BaseTest {
    private AuthPage authPage = new AuthPage();

    @Test
    @DisplayName("Успешная авторизация")
    @Description("Проверка входа с корректными учетными данными")
    public void testSuccessfulLogin() {
        authPage.login(testUser)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверка ошибки при авторизации с некорректным паролем")
    public void testLoginWithWrongPassword() {
        User wrongUser = new User(testUser.getEmail(), "wrongpassword", null);

        authPage.login(wrongUser)
                .then()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неверным email")
    @Description("Проверка ошибки при авторизации с некорректным email")
    public void testLoginWithWrongEmail() {
        User wrongUser = new User("wrong@example.com", testUser.getPassword(), null);

        authPage.login(wrongUser)
                .then()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }
}