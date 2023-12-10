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

public class CourierTests {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    private int id;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }


    @Test
    @DisplayName("Создание курьера")
    public void CreateCourier() {

        Courier courier = new Courier("HermioneGranger", "Hermione", "Hermione");

        CourierClient courierClient = new CourierClient();

        Response response = courierClient.create(courier);

        response.then().assertThat().body("ok", equalTo(true));  //Проверяем, что успешный запрос возвращает ok: true;
        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());


        //Войти под логином
        Response loginResponse = CourierClient.login(fromCourier(courier));
        id = loginResponse.path("id");
       assertEquals("Неверный статус код", SC_OK, loginResponse.statusCode());

    }

    @Test
    @DisplayName("Создание двух курьеров с одним логином")
    @Description ("Нельзя создать двух одинаковых курьеров")
    public void checkDoubleCreateNewCourier() {

        Courier courier = new Courier("HermioneGranger", "Hermione", "Hermione");

        CourierClient courierClient = new CourierClient();
        Response responseFirstCourier = courierClient.create(courier);
        assertEquals("Неверный статус код", SC_CREATED, responseFirstCourier.statusCode());

      Response responseSecondCourier = courierClient.create(courier);
        responseSecondCourier.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(SC_CONFLICT);

       System.out.println(responseSecondCourier.body().asString());

    }
    @Test
    @DisplayName("Создание курьера без пароля")
    public void checkCourierResponseWithoutFieldBodyTest() {
        Courier courierWithoutPassword = new Courier("HermioneGranger", "", "Hermione");

        CourierClient courierClient = new CourierClient();
        Response response = courierClient.create(courierWithoutPassword);

        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(SC_BAD_REQUEST);

        System.out.println(response.body().asString());

    }

    @After
    public void tearDown() {
        Courier courierDelete = new Courier("HermioneGranger", "Hermione", "Hermione");

        Response loginResponse = CourierClient.login(fromCourier(courierDelete));

        assertEquals("Неверный статус код", SC_OK, loginResponse.statusCode());

        id = loginResponse.path("id");
        System.out.println(id);

        Response responseDelete = CourierClient.delete(id);

        responseDelete.then().assertThat().body("ok", equalTo(true));  //Проверяем, что успешный запрос возвращает ok: true;
        assertEquals("Неверный статус код", SC_OK, responseDelete.statusCode());

    }


}
