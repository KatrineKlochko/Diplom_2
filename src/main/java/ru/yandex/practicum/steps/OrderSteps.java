package ru.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.model.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    public static final String ORDERS = "/api/orders";
    public static final String ING_1 = "61c0c5a71d1f82001bdaaa6d";
    public static final String ING_2 = "61c0c5a71d1f82001bdaaa6f";

    @Step("Метод создания заказа")
    public ValidatableResponse createOrder(String token, Order order){
        return given()
                .header("Authorization", token != null ? token : "")
                .body(order)
                .when()
                .post(ORDERS)
                .then();
    }

}
