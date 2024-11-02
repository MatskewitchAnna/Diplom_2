package praktikum.orders;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.user.UserData;
import praktikum.user.UserSteps;
import service.Service;

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
