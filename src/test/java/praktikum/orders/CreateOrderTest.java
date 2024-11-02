package ru.yandex.praktikum.order;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.*;
import service.IngredientsResponse;
import service.Service;
import ru.yandex.praktikum.user.UserSteps;
import ru.yandex.praktikum.user.UserData;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderTest {

    UserSteps userSteps = new UserSteps();
    OrderSteps orderSteps = new OrderSteps();
    String authToken;
    IngredientsResponse ingredients;
    Order order;

    @Before
    @Step("Создание пользователя")
    public void setUp() {
        Service.setupSpecification();
        Response loginResponse = userSteps.createUser(UserData.user);
        authToken = loginResponse.then().extract().path("accessToken");
    }

    @After
    @Step("Удаление пользователя")
    public void tearDown() {
        userSteps.deleteUser(authToken, UserData.user);
    }

    @Test
    @DisplayName("Авторизация, создание заказа")
    public void createOrderWithAuthTest() {
        ingredients = orderSteps.getAllIngredients();
        ArrayList<String> tempOrder = new OrderRandomData().createRandomOrder(ingredients);
        order = new Order(tempOrder);
        Response response = orderSteps.createOrderWithAuth(authToken, order);
        orderSteps.checkResponseOfCreateOrder(response);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthTest() {
        ingredients = orderSteps.getAllIngredients();
        ArrayList<String> tempOrder = new OrderRandomData().createRandomOrder(ingredients);
        order = new Order(tempOrder);
        Response response = orderSteps.createOrderWithoutAuth(order);
        orderSteps.checkResponseOfCreateOrderWithoutAuth(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        ArrayList<String> tempOrder = new ArrayList<>(List.of(new String[]{}));
        order = new Order(tempOrder);
        Response response = orderSteps.createOrderWithAuth(authToken, order);
        orderSteps.checkResponseOfCreateOrderWithoutIngredients(response);
    }

    @Test
    @DisplayName("Создание заказа с неверным хэшем ингредиентов")
    public void createOrderWithIncorrectIngredientsTest() {
        ArrayList<String> tempOrder = new ArrayList<>(List.of(new String[]{"1", "2"}));
        order = new Order(tempOrder);
        Response response = orderSteps.createOrderWithAuth(authToken, order);
        orderSteps.checkResponseOfCreateOrderWithIncorrectIngredients(response);
    }
}
