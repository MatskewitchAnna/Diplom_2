package ru.yandex.praktikum.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import service.AuthResponse;
import service.Service;


public class CreateUserTest {

    AuthResponse regResponse;
    UserSteps userSteps = new UserSteps();

    @Before
    public void setUp() {
        Service.setupSpecification();
    }

    @Test
    @DisplayName("Создание нового пользователя и проверка ответа")
    public void createNewUserTest() {
        regResponse = userSteps.createUserAuthResponse(UserData.user);
        userSteps.checkBodyOfResponse(regResponse);
        userSteps.deleteUser(regResponse.getAccessToken(), UserData.user);
    }

    @Test
    @DisplayName("Создание существующего пользователя и проверка ответов")
    public void createExistingUserTest() {
        regResponse = userSteps.createUserAuthResponse(UserData.user);
        userSteps.checkBodyOfResponse(regResponse);
        Response newResponse = userSteps.createUser(UserData.user);
        userSteps.checkStatusCodeForbidden(newResponse);
        userSteps.checkBodyOfExistingUser(newResponse);
        userSteps.deleteUser(regResponse.getAccessToken(), UserData.user);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameTest() {
        Response response = userSteps.createUserWithoutName();
        userSteps.checkStatusCodeForbidden(response);
        userSteps.checkMessageToRequestWithoutAnyField(response);
    }

    @Test
    @DisplayName("Создание пользователя без почты")
    public void createUserWithoutEmailTest() {
        Response response = userSteps.createUserWithoutEmail();
        userSteps.checkStatusCodeForbidden(response);
        userSteps.checkMessageToRequestWithoutAnyField(response);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPasswordTest() {
        Response response = userSteps.createUserWithoutPassword();
        userSteps.checkStatusCodeForbidden(response);
        userSteps.checkMessageToRequestWithoutAnyField(response);
    }
}
