package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.steps.OrderSteps;
import ru.yandex.practicum.steps.UserSteps;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.apache.http.HttpStatus.*;

import java.util.Arrays;
import java.util.Collections;


public class CreateOrderTests extends BaseTest{

    private UserSteps userSteps = new UserSteps();
    private OrderSteps orderSteps = new OrderSteps();

    private User user;
    private String accessToken;

    @Before
    public void setUp() {

        user = new User();
        user
                .setEmail(RandomStringUtils.randomAlphabetic(10) + "@ya.ru")
                .setPassword(RandomStringUtils.randomAlphabetic(10))
                .setName(RandomStringUtils.randomAlphabetic(10));
        userSteps
                .createUser(user);
        accessToken = userSteps
                .loginUser(user)
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Проверка создания заказа с авторизацией")
    @Description("Тест на успешное создание заказа с ингридиентами и авторизацией для /api/orders эндпоинт")
    public void shouldCreateOrderWithAuthTest() {

        Order order = new Order(Arrays.asList(OrderSteps.ING_BUN, OrderSteps.ING_MAIN));

        orderSteps
                .createOrder(accessToken, order)
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Проверка создания заказа без авторизации")
    @Description("Тест на успешное создание заказа с ингридиентами без авторизации для /api/orders эндпоинт")
    public void shouldCreateOrderWithoutAuthTest() {

        Order order = new Order(Arrays.asList(OrderSteps.ING_BUN, OrderSteps.ING_MAIN));

        orderSteps
                .createOrder(null, order)
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Проверка создания заказа без ингридиентов")
    @Description("Тест на невозможность создания заказа без ингридиентов для /api/orders эндпоинт")
    public void shouldNotCreateOrderWithoutIngredientsTest() {

        Order order = new Order(Collections.emptyList());

        orderSteps
                .createOrder(accessToken, order)
                .statusCode(SC_BAD_REQUEST)
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Проверка создания заказа с неверным хешем ингредиента")
    @Description("Тест на невозможность создания заказа с неверным хешем ингредиента для /api/orders эндпоинт. Тест упал, фактический код ответа не соответствует документации, 400 Bad Request")
    public void shouldNotCreateOrderWithInvalidHashTest() {

        Order order = new Order(Collections.singletonList("invalid_hash"));

        orderSteps
                .createOrder(accessToken, order)
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown(){

        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }

}
