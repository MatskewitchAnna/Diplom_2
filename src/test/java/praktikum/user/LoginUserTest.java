package praktikum.user;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import service.AuthResponse;
import service.Service;

public class LoginUserTest {

    UserSteps userSteps = new UserSteps();
    AuthResponse loginResponse;
    Response response;

    @Before
    @Step("Создание пользователя")
    public void setUp() {
        Service.setupSpecification();
        userSteps.createUser(UserData.user);
    }

    @Test
    @DisplayName("Проверка авторизации")
    public void checkLoginUserTest() {
        loginResponse = userSteps.loginUserAuthResponse(UserData.from(UserData.user));
        userSteps.checkBodyOfResponse(loginResponse);
        userSteps.deleteUser(loginResponse.getAccessToken(), UserData.user);
    }

    @Test
    @DisplayName("Авторизация пользователя с неправильными данными и проверка ответа")
    public void loginIncorrectUserTest() {
        response = userSteps.loginIncorrectUser();
        userSteps.checkStatusCodeWithIncorrectData(response);
        userSteps.checkMessageWithIncorrectData(response);
    }

}
