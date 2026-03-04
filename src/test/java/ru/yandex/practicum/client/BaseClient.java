package ru.yandex.practicum.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

// Базовый клиент с общими настройками для всех API-запросов
public class BaseClient {

    // Базовый URL сервиса Stellar Burgers
    protected static final String BASE_URL = "https://stellarburgers.education-services.ru";

    // Создание запроса с базовым URL и json
    protected RequestSpecification getSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .build();
    }
}