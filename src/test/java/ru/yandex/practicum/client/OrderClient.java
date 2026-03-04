package ru.yandex.practicum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.model.Order;

import static io.restassured.RestAssured.given;

// Клиент для работы с эндпоинтами заказов и ингредиентов
public class OrderClient extends BaseClient {

    // Путь к эндпоинту заказов
    private static final String ORDERS_PATH = "/api/orders";
    // Путь к эндпоинту получения списка ингредиентов
    private static final String INGREDIENTS_PATH = "/api/ingredients";

    // Создание заказа с токеном авторизации в заголовке
    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(Order order, String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }

    // Создание заказа без токена
    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }

    // Получение списка всех доступных ингредиентов
    @Step("Получение ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(getSpec())
                .when()
                .get(INGREDIENTS_PATH);
    }
}