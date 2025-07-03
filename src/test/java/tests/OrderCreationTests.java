package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.Assume;
import page.OrderPage;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class OrderCreationTests extends BaseTest {
    private final OrderPage orderPage = new OrderPage();

    @Test
    @DisplayName("Создание авторизированного заказа с ингредиентами")
    @Description("Проверка создания заказа авторизованным пользователем с валидными ингредиентами")
    public void testCreateOrderWithAuthAndIngredients() {
        List<String> ingredients = getAvailableIngredients();

        orderPage.createOrder(ingredients, authToken)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка ошибки при создании заказа без авторизации")
    public void testCreateOrderWithoutAuth() {
        List<String> ingredients = orderPage.getIngredients();
        Assume.assumeFalse("Нет доступных ингредиентов", ingredients.isEmpty());

        orderPage.createOrderWithoutAuth(ingredients)
                .then()
                .statusCode(401)
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка создания заказа без ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        orderPage.createOrderWithoutIngredients()
                .then()
                .statusCode(400)
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с некорректным хешем ингредиента")
    @Description("Проверка ошибки при создании заказа с некорректным хешем ингредиентов")
    public void testCreateOrderWithInvalidIngredient() {
        orderPage.createOrderWithInvalidIngredient()
                .then()
                .statusCode(500);
    }
}