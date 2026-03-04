package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.client.UserClient;
import ru.yandex.practicum.model.User;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest {

    private UserClient userClient;
    private String accessToken;
    private User user;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = new User("test-login-" + System.currentTimeMillis() + "@yandex.ru", "password123", "TestUser");
        Response response = userClient.createUser(user);
        accessToken = response.path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Проверка успешной авторизации с валидными данными")
    public void loginExistingUserTest() {
        Response response = userClient.loginUser(user);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным email")
    @Description("Проверка авторизации с неверным логином")
    public void loginWithWrongEmailTest() {
        User wrongUser = new User("wrong-email@yandex.ru", "password123", "TestUser");

        Response response = userClient.loginUser(wrongUser);

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Проверка авторизации с неверным паролем")
    public void loginWithWrongPasswordTest() {
        User wrongUser = new User(user.getEmail(), "wrongpassword", "TestUser");

        Response response = userClient.loginUser(wrongUser);

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
