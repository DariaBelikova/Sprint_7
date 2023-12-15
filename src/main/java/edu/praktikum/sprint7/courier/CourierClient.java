package edu.praktikum.sprint7.courier;

import models.Courier;
import models.CourierCreds;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String COURIER_URL = "api/v1/courier";
    private static final String COURIER_LOGIN_URL = "api/v1/courier/login";
    private static final String COURIER_DELETE_URL = "/api/v1/courier/%s";

    private static int id;



    @Step("Создание курьера {courier}")
    public Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER_URL);
    }

    @Step("Авторизация курьером с кредами {courierCreds}")
    public static Response login(CourierCreds courierCreds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierCreds)
                .when()
                .post(COURIER_LOGIN_URL);
    }

    @Step("Авторизация курьером с неправильным логином или паролем")
    public static Response loginBadPassword(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER_LOGIN_URL);
    }

    @Step("Удаление курьера")
    public static Response delete(int id) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(String.format(COURIER_DELETE_URL, id));
    }

}
