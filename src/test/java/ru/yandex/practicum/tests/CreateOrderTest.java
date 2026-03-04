package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.client.UserClient;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;
    private List<String> ingredientIds;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();

        User user = new User("test-order-" + System.currentTimeMillis() + "@yandex.ru", "password123", "TestUser");
        Response registerResponse = userClient.createUser(user);
        accessToken = registerResponse.path("accessToken");

        Response ingredientsResponse = orderClient.getIngredients();
        ingredientIds = ingredientsResponse.path("data._id");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Проверка успешного создания заказа авторизованным пользователем")
    public void createOrderWithAuthAndIngredientsTest() {
        List<String> ingredients = List.of(ingredientIds.get(0), ingredientIds.get(1));
        Order order = new Order(ingredients);

        Response response = orderClient.createOrderWithAuth(order, accessToken);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка создания заказа без токена авторизации")
    public void createOrderWithoutAuthTest() {
        List<String> ingredients = List.of(ingredientIds.get(0), ingredientIds.get(1));
        Order order = new Order(ingredients);

        Response response = orderClient.createOrderWithoutAuth(order);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка что нельзя создать заказ без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        Order order = new Order(new ArrayList<>());

        Response response = orderClient.createOrderWithAuth(order, accessToken);

        response.then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка что нельзя создать заказ с невалидным хешем ингредиента")
    public void createOrderWithInvalidHashTest() {
        List<String> ingredients = List.of("invalidhash123456");
        Order order = new Order(ingredients);

        Response response = orderClient.createOrderWithAuth(order, accessToken);

        response.then()
                .statusCode(500);
    }
}
