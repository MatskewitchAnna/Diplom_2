package praktikum.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import service.AuthResponse;
import service.Service;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class UserSteps {

    private static final String USER_PATH = "auth/user";
    private static final String USER_CREATE_PATH = "auth/register";
    private static final String USER_LOGIN_PATH = "auth/login";
    public static final String USER_DELETE_PATH = "auth/user";
    private static final String USER_EDIT_PATH = "auth/user";

    @Step("Создание пользователя")
    public Response createUser(User user) {
        Service.setupSpecification();
        return given()
                .body(user)
                .when()
                .post(USER_CREATE_PATH);
    }

    @Step("Создание пользователя + возвращение результата классом")
    public AuthResponse createUserAuthResponse(User user) {
        Service.setupSpecification();
        return given()
                .body(user)
                .when()
                .post(USER_CREATE_PATH)
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .response()
                .as(AuthResponse.class);
    }

    @Step("Запрос на создание пользователя без имени")
    public Response createUserWithoutName() {
        return createUser(UserData.userWithoutName);
    }

    @Step("Запрос на создание пользователя без почты")
    public Response createUserWithoutEmail() {
        return createUser(UserData.userWithoutEmail);
    }

    @Step("Запрос на создание пользователя без пароля")
    public Response createUserWithoutPassword() {
        return createUser(UserData.userWithoutPassword);
    }

    @Step("Проверка отстутствия необходимого поля")
    public void checkMessageToRequestWithoutAnyField (Response response) {
        response.then().body("success", is(false))
                .and().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Проверка ошибки создания существующего пользователя")
    public void checkBodyOfExistingUser(Response response) {
        response.then().body("success", is(false)).
                assertThat().body("message", equalTo("User already exists"));
    }

    @Step("Удаление пользователя")
    public void deleteUser(String accessToken, User user) {
        given()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .delete(USER_DELETE_PATH)
                .then()
                .statusCode(HTTP_ACCEPTED);
    }

    @Step("Авторизация пользователя")
    public Response loginUser(UserData user) {
        Service.setupSpecification();
        return given()
                .body(user)
                .when()
                .post(USER_LOGIN_PATH);
    }

    @Step("Авторизация + возвращение результата классом")
    public AuthResponse loginUserAuthResponse(UserData user) {
        return given()
                .body(user)
                .when()
                .post(USER_LOGIN_PATH)
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .response()
                .as(AuthResponse.class);
    }

    @Step("Авторизация пользователя с неправильными данными")
    public Response loginIncorrectUser() {
        return loginUser(UserData.from(UserData.newUser));
    }

    @Step("Получение информации о пользователе")
    public Response getUser(User user) {
        Service.setupSpecification();
        return given()
                .when()
                .post(USER_PATH);
    }

    @Step("Изменение данных пользователя")
    public Response editUserData(String accessToken, User user) {
        Service.setupSpecification();
        return given()
                .header("authorization", accessToken)
                .body(user)
                .when()
                .patch(USER_EDIT_PATH);
    }

    @Step("Изменение данных пользователя без авторизации")
    public Response editUserDataWithoutToken(User user){
        Service.setupSpecification();
        return given()
                .body(user)
                .when()
                .patch(USER_EDIT_PATH);
    }

    @Step("Проверка ответов")
    public void checkBodyOfResponse(AuthResponse response) {
        assertTrue(response.isSuccess());
        assertEquals(UserData.userEmail, response.getUser().getEmail());
        assertEquals(UserData.userName, response.getUser().getName());
        assertFalse(response.getAccessToken().isBlank());
        assertFalse(response.getRefreshToken().isBlank());
    }

    @Step("Проверка статуса ответа (FORBIDDEN)")
    public void checkStatusCodeForbidden(Response response) {
        assertEquals(HTTP_FORBIDDEN, response.getStatusCode());
    }

    @Step("Проверка тела ответа (ошибка авторизации)")
    public void checkMessageWithIncorrectData (Response response){
        response.then()
                .body("success", is(false))
                .and()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Step("Проверка статуса ответа (UNAUTHORIZED)")
    public void checkStatusCodeWithIncorrectData(Response response) {
        response.then().statusCode(HTTP_UNAUTHORIZED);
    }
}
