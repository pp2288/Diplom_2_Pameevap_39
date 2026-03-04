package ru.yandex.practicum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.model.User;

import static io.restassured.RestAssured.given;

// Клиент для работы с эндпоинтами пользователя (регистрация, логин, удаление)
public class UserClient extends BaseClient {

    // Путь к эндпоинту регистрации
    private static final String REGISTER_PATH = "/api/auth/register";
    // Путь к эндпоинту авторизации
    private static final String LOGIN_PATH = "/api/auth/login";
    // Путь к эндпоинту работы с данными пользователя
    private static final String USER_PATH = "/api/auth/user";

    // Регистрация нового пользователя (POST-запрос с email, пароль, name)
    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(REGISTER_PATH);
    }

    // Авторизация пользователя по email и паролю
    @Step("Логин пользователя")
    public Response loginUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(LOGIN_PATH);
    }

    // Удаление пользователя после тестирования
    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(USER_PATH);
    }
}