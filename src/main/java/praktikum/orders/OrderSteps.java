package praktikum.orders;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import service.IngredientsResponse;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.*;

public class OrderSteps {

    public static final String ORDER_PATH = "orders";

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(String accessToken, Order order) {
        return given()
                .header("authorization", accessToken)
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Проверка ответа после создания заказа")
    public void checkResponseOfCreateOrder(Response response) {
        response.then()
                .body("success",is(true))
                .and()
                .body("name", notNullValue())
                .body("order",notNullValue())
                .statusCode(HTTP_OK);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(Order order) {
        return given()
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Получение списка заказов пользователя")
    public Response getUsersOrders(String accessToken) {
        return given()
                .header("authorization", accessToken)
                .when()
                .get(ORDER_PATH);
    }

    @Step("Проверка ответа получения списка заказов")
    public void checkResponseWhenGetListOfOrders (Response response) {
        response.then()
                .body("success",is(true))
                .and()
                .body("orders",notNullValue())
                .body("total",notNullValue())
                .body("totalToday",notNullValue())
                .statusCode(HTTP_OK);
    }

    @Step("Получение заказов без авторизации")
    public Response getOrdersWithoutAuth() {
        return given()
                .when()
                .get(ORDER_PATH);
    }

    @Step("Проверка ответа на создание заказа без авторизации")
    public void checkResponseOfCreateOrderWithoutAuth(Response response) {
        response.then()
                .body("success",is(true))
                .and()
                .body("name", notNullValue())
                .body("order",notNullValue())
                .statusCode(HTTP_OK);
    }

    @Step("Проверка ответа получения списка заказов без авторизации")
    public void checkResponseWhenGetListOfOrdersWithoutAuthorization(Response response) {
        response.then()
                .body("success",is(false))
                .and()
                .body("message",equalTo("You should be authorised"))
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Step("Получение классом списка возможных ингредиентов")
    public IngredientsResponse getAllIngredients() {
        return given()
                .when()
                .get("ingredients")
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .response()
                .as(IngredientsResponse.class);
    }

    @Step("Проверка ответа, заказ без ингредиентов")
    public void checkResponseOfCreateOrderWithoutIngredients (Response response) {
        response.then()
                .body("success",is(false))
                .and()
                .body("message",equalTo("Ingredient ids must be provided"))
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Step("Проверка ответа, заказ с нвеерным хэшем ингредиентов")
    public void checkResponseOfCreateOrderWithIncorrectIngredients(Response response) {
        response.then()
                .statusCode(HTTP_SERVER_ERROR);
    }
}
