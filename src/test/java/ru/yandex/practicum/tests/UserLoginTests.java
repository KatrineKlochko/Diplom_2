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
import static org.apache.http.HttpStatus.*;

public class UserLoginTests extends BaseTest{

    private UserSteps userSteps = new UserSteps();
    private User user;

    @Before
    public void setUp(){

        user = new User();
        user
                .setEmail((RandomStringUtils.randomAlphabetic(12)) + "@ya.ru")
                .setPassword(RandomStringUtils.randomAlphabetic(12))
                .setName(RandomStringUtils.randomAlphabetic(12));
        userSteps
                .createUser(user);
    }

    @Test
    @DisplayName("Проверка на логин пользователя")
    @Description("Тест на успешный логин пользователя для /api/auth/login эндпоинт")
    public void shouldLoginUserTest(){
        userSteps
                .loginUser(user)
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Проверка на авторизацию пользователя с неверным паролем")
    @Description("Тест на невозможность авторизации пользователя с неверным паролем для /api/auth/login эндпоинт")
    public void shouldNotLoginUserWithNonExistentPasswordTest(){
        User nonExistingUser = new User(user)
                .setPassword(RandomStringUtils.randomAlphabetic(12));
        userSteps
                .loginUser(nonExistingUser)
                .statusCode(SC_UNAUTHORIZED)
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверка на авторизацию пользователя с неверным логином")
    @Description("Тест на невозможность авторизации пользователя с неверным логином для /api/auth/login эндпоинт")
    public void shouldNotLoginUserWithNonExistentLoginTest(){
        User nonExistingUser = new User(user)
                .setEmail((RandomStringUtils.randomAlphabetic(12)) + "@ya.ru");
        userSteps
                .loginUser(nonExistingUser)
                .statusCode(SC_UNAUTHORIZED)
                .body("message", is("email or password are incorrect"));
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
