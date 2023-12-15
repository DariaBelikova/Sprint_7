package edu.praktikum.sprint7;

import edu.praktikum.sprint7.courier.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static models.CourierCreds.fromCourier;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class CourierLoginTests {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private int id;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        Courier courier = new Courier("RonWeasley", "RonWeasley", "Ron");
        CourierClient courierClient = new CourierClient();
        courierClient.create(courier);

    }


    @Test
    @DisplayName("Проверка правильного ввода пароля")
    @Description("Сервис вернет ошибку, если указать неправильный пароль")
    public void checkCourierLoginBadPasswordResponseBodyTest() {
        Courier courierWithoutPassword = new Courier("RonWeasley", "Ron", "Ron");
        Response loginResponse = CourierClient.loginBadPassword(courierWithoutPassword);

        loginResponse.then().assertThat().statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));


    }

    @After
    public void tearDown() {
        Courier courierDelete = new Courier("RonWeasley", "RonWeasley", "Ron");

        Response loginResponse = CourierClient.login(fromCourier(courierDelete));

        id = loginResponse.path("id");

        Response responseDelete = CourierClient.delete(id);

        assertEquals("Неверный статус код", SC_OK, responseDelete.statusCode());
        responseDelete.then().assertThat().body("ok", equalTo(true));  //Проверяем, что успешный запрос возвращает ok: true;


    }
}
