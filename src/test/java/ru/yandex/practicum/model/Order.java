package ru.yandex.practicum.model;

import java.util.List;

public class Order {
    private List<String> ingredients;

    // Конструктор с указанием списка ингредиентов
    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}