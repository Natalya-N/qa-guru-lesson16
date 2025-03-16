package tests;

import io.restassured.RestAssured;
import models.pojo.UserRequestModel;
import models.pojo.UserResponseModel;
import models.pojo.UserFullResponseModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("simple")
public class ReqresTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    @DisplayName("Проверить успешное создание пользователя")
    void successfulCreateUserTest() {
        UserRequestModel request = new UserRequestModel();
        request.setName("Natalya");
        request.setJob("QA");

        UserResponseModel response = step("Создать нового пользователя, указать имя и работу",
                () -> given()
                        .body(request)
                        .contentType(JSON)
                        .log().uri()
                        .log().body()
                        .log().headers()
                        .when()
                        .post("/users")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(201)
                        .extract().as(UserResponseModel.class));

        step("Проверить, что в ответе вернулись указанные в запросе данные", () -> {
            assertEquals(response.getName(), "Natalya");
            assertEquals(response.getJob(), "QA");
            assertThat(!response.getId().isEmpty());
            assertThat(!response.getCreatedAt().isEmpty());
        });
    }

    @Test
    @DisplayName("Проверить успешное получение пользователя")
    void successfulGetUserTest() {
        UserFullResponseModel response = step("Получить пользователя по id",
                () -> given()
                        .contentType(JSON)
                        .log().uri()
                        .when()
                        .get("/users/2")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(UserFullResponseModel.class));

        step("Проверить, что в ответе вернулись данные о пользователе", () -> {
            assertEquals(response.getData().getId(), "2");
            assertEquals(response.getData().getEmail(), "janet.weaver@reqres.in");
            assertEquals(response.getData().getFirstName(), "Janet");
            assertEquals(response.getData().getLastName(), "Weaver");
            assertEquals(response.getData().getAvatar(), "https://reqres.in/img/faces/2-image.jpg");
            assertEquals(response.getSupport().getUrl(), "https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral");
            assertEquals(response.getSupport().getText(), "Tired of writing endless social media content? Let Content Caddy generate it for you.");
        });
    }

    @Test
    @DisplayName("Проверить успешное обновление пользователя")
    void successfulUpdateUserTest() {
        UserRequestModel request = new UserRequestModel();
        request.setName("Natalya");
        request.setJob("AQA");

        UserResponseModel response = step("Изменить данные о пользователе",
                () -> given()
                        .body(request)
                        .contentType(JSON)
                        .log().uri()
                        .log().body()
                        .log().headers()
                        .when()
                        .put("/users/95")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(UserResponseModel.class));

        step("Проверить, что в ответе вернулись обновленные данные", () -> {
            assertEquals(response.getName(), "Natalya");
            assertEquals(response.getJob(), "AQA");
            assertThat(!response.getUpdatedAt().isEmpty());
        });
    }

    @Test
    @DisplayName("Проверить успешное удаление пользователя")
    void successfulDeleteUserTest() {
        step("Удалить пользователя по id",
                () -> given()
                        .contentType(JSON)
                        .log().uri()
                        .when()
                        .delete("/users/2")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(204));
    }

    @Test
    @DisplayName("Проверить, что для несуществующего пользователя возвращается код 404")
    void unsuccessfulUserNotFoundTest() {
        step("Получить пользователя по несуществующему id",
                () -> given()
                        .contentType(JSON)
                        .log().uri()
                        .when()
                        .get("/users/23")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(404));
    }

}
