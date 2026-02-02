package ru.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.model.User;

import static io.restassured.RestAssured.given;

public class UserSteps {

    public static final String USER = "/api/auth/register";
    public static final String LOGIN = "/api/auth/login";
    public static final String DELETE = "/api/auth/user";

    @Step("Метод создания пользователя")
    public ValidatableResponse createUser (User user){
        return given()
                .body(user)
                .when()
                .post(USER)
                .then();
    }

    @Step("Метод логина пользователя")
    public ValidatableResponse loginUser (User user){
        return given()
                .body(user)
                .when()
                .post(LOGIN)
                .then();
    }

    @Step("Метод удаления пользователя")
    public ValidatableResponse deleteUser (String token){
        return given()
                .header("Authorization", token)
                .when()
                .delete(DELETE)
                .then();
    }
}
