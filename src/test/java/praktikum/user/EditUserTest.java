package praktikum.user;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.Service;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class EditUserTest {

    UserSteps userSteps = new UserSteps();
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
    @DisplayName("Изменение данных пользователя")
    public void editUserDataTest() {
        userSteps.loginUserAuthResponse(UserData.from(UserData.user));
        Response response = userSteps.editUserData(authToken, UserData.newUser);
        response.then()
                .assertThat()
                .body("success", is( true))
                .body ("user.name", equalTo(UserData.newUserName))
                .body ("user.email", equalTo(UserData.newUserEmail));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void editUserDataWithoutAuthTest() {
        Response response = userSteps.editUserDataWithoutToken(UserData.newUser);
        response.then()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }

}
