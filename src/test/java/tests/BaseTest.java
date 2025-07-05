package tests;

import constants.Endpoints;

import io.restassured.RestAssured;
import model.User;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import page.AuthPage;
import page.OrderPage;

import java.util.List;

public class BaseTest {
    protected User testUser;
    protected String authToken;
    protected AuthPage authPage = new AuthPage();
    protected OrderPage orderPage = new OrderPage();

    @Before
    public void setUp() {
        RestAssured.baseURI = Endpoints.BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // Создание тестового пользователя
        testUser = new User(
                "test-user-" + System.currentTimeMillis() + "@yandex.ru",
                "password",
                "Test User"
        );

        authToken = authPage.register(testUser)
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");
    }

    @After
    public void tearDown() {
        if (authToken != null) {
            authPage.deleteUser(authToken)
                    .then()
                    .statusCode(202);
        }
    }

    protected List<String> getAvailableIngredients() {
        List<String> ingredients = orderPage.getIngredients();
        Assume.assumeFalse("Нет доступных ингредиентов", ingredients.isEmpty());
        return ingredients;
    }
}