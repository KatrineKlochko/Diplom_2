package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.steps.UserSteps;

import static org.hamcrest.CoreMatchers.is;

public class UserCreateTests extends BaseTest {

    private UserSteps userSteps = new UserSteps();
    private User user;

    @Before
    public void setUp(){

        user = new User();
        user
                .setEmail((RandomStringUtils.randomAlphabetic(12)) + "@ya.ru")
                .setPassword(RandomStringUtils.randomAlphabetic(12))
                .setName(RandomStringUtils.randomAlphabetic(12));
    }

    @Test
    @DisplayName("Проверка на создание пользователя")
    @Description("Тест на успешное создание пользователя для /api/auth/register эндпоинт")
    public void shouldCreateUserTest(){
        userSteps
                .createUser(user)
                .statusCode(200)
                .body("success", is(true))
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Проверка на создание двух одинаковых пользователей")
    @Description("Тест на невозможность создания двух одинаковых пользователей для /api/auth/register эндпоинт")
    public void shouldNotCreateSameUserTest(){
        userSteps
                .createUser(user);
        userSteps
                .createUser(user)
                .statusCode(403)
                .body("message", is("User already exists"));
    }

    @Test
    @DisplayName("Проверка на создание пользователя без email")
    @Description("Тест на невозможность создания пользователя с незаполненным полем email для /api/auth/register эндпоинт")
    public void shouldNotCreateUserWithoutEmail(){
        user.setEmail("");
        userSteps
                .createUser(user)
                .statusCode(403)
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка на создание пользователя без пароля")
    @Description("Тест на невозможность создания пользователя с незаполненным полем пароль для /api/auth/register эндпоинт")
    public void shouldNotCreateUserWithoutPassword(){
        user.setPassword("");
        userSteps
                .createUser(user)
                .statusCode(403)
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка на создание пользователя без имени")
    @Description("Тест на невозможность создания пользователя с незаполненным полем имя для /api/auth/register эндпоинт")
    public void shouldNotCreateUserWithoutName(){
        user.setName("");
        userSteps
                .createUser(user)
                .statusCode(403)
                .body("message", is("Email, password and name are required fields"));
    }


    @After
    public void tearDown(){

        String accessToken = userSteps
                .loginUser(user)
                .extract()
                .body()
                .path("accessToken");
        if (accessToken != null) {
            user.setAccessToken(accessToken);
            userSteps.deleteUser(accessToken);
        }
    }

}
