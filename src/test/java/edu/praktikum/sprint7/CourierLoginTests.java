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
        Response responseFirstCourier = courierClient.create(courier);
        assertEquals("Неверный статус код", SC_CREATED, responseFirstCourier.statusCode());
    }


    @Test
    @DisplayName("Проверка правильного ввода пароля")
    @Description("Сервис вернет ошибку, если указать неправильный пароль")
    public void checkCourierLoginBadPasswordResponseBodyTest() {
        Courier courierWithoutPassword = new Courier("RonWeasley", "Ron", "Ron");
        CourierClient courierClient = new CourierClient();
        Response loginResponse = courierClient.loginBadPassword(courierWithoutPassword);

        loginResponse.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);

        System.out.println(loginResponse.body().asString());

    }

    @After
    public void tearDown() {
        Courier courierDelete = new Courier("RonWeasley", "RonWeasley", "Ron");

        Response loginResponse = CourierClient.login(fromCourier(courierDelete));

        assertEquals("Неверный статус код", SC_OK, loginResponse.statusCode());

        id = loginResponse.path("id");
        System.out.println(id);

        Response responseDelete = CourierClient.delete(id);

        responseDelete.then().assertThat().body("ok", equalTo(true));  //Проверяем, что успешный запрос возвращает ok: true;
        assertEquals("Неверный статус код", SC_OK, responseDelete.statusCode());

    }
}
