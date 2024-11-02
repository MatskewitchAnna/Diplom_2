package ru.yandex.praktikum.order;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.*;
import service.Service;
import ru.yandex.praktikum.user.UserSteps;
import ru.yandex.praktikum.user.UserData;

public class OrderListTest {

    UserSteps userSteps = new UserSteps();
    OrderSteps orderSteps = new OrderSteps();
    String authToken;

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
    @DisplayName("Получение списка заказов авторизованного пользователя")
    public void getUsersOrdersTest() {
        Response response = orderSteps.getUsersOrders(authToken);
        orderSteps.checkResponseWhenGetListOfOrders(response);
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации")
    public void getOrdersWithoutAuthTest() {
        Response response = orderSteps.getOrdersWithoutAuth();
        orderSteps.checkResponseWhenGetListOfOrdersWithoutAuthorization(response);
    }
}
