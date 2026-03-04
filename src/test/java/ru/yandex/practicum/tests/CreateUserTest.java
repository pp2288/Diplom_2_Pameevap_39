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

public class CreateUserTest {

    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешного создания нового пользователя")
    public void createUniqueUserTest() {
        User user = new User("test-user-" + System.currentTimeMillis() + "@yandex.ru", "password123", "TestUser");

        Response response = userClient.createUser(user);
        accessToken = response.path("accessToken");

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка что нельзя создать двух одинаковых пользователей")
    public void createDuplicateUserTest() {
        User user = new User("test-user-" + System.currentTimeMillis() + "@yandex.ru", "password123", "TestUser");

        Response firstResponse = userClient.createUser(user);
        accessToken = firstResponse.path("accessToken");

        Response secondResponse = userClient.createUser(user);
        secondResponse.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверка что нельзя создать пользователя без обязательного поля email")
    public void createUserWithoutEmailTest() {
        User user = new User(null, "password123", "TestUser");

        Response response = userClient.createUser(user);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверка что нельзя создать пользователя без обязательного поля password")
    public void createUserWithoutPasswordTest() {
        User user = new User("test-user-" + System.currentTimeMillis() + "@yandex.ru", null, "TestUser");

        Response response = userClient.createUser(user);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка что нельзя создать пользователя без обязательного поля name")
    public void createUserWithoutNameTest() {
        User user = new User("test-user-" + System.currentTimeMillis() + "@yandex.ru", "password123", null);

        Response response = userClient.createUser(user);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
