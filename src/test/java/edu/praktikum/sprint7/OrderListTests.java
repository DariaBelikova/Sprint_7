package edu.praktikum.sprint7;

import edu.praktikum.sprint7.courier.CourierClient;
import edu.praktikum.sprint7.order.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static models.CourierCreds.fromCourier;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class OrderListTests {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private int id;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        Courier courier = new Courier("luna", "lavgut", "luna");
        CourierClient courierClient = new CourierClient();
        courierClient.create(courier);

    }

    @Test
    @DisplayName("Получение списка заказов")
    public void checkOrdersBodyTest() {
        Courier courier = new Courier("luna", "lavgut", "luna");

        Response loginResponse = CourierClient.login(fromCourier(courier));

        id = loginResponse.path("id");
        Response responseList = OrderClient.getOrderList(id);
        assertEquals("Неверный статус код", SC_OK, responseList.statusCode());


    }

    @After
    public void tearDown() {
        Courier courier = new Courier("luna", "lavgut", "luna");

        Response loginResponse = CourierClient.login(fromCourier(courier));

        id = loginResponse.path("id");

        Response responseDelete = CourierClient.delete(id);

        assertEquals("Неверный статус код", SC_OK, responseDelete.statusCode());
        responseDelete.then().assertThat().body("ok", equalTo(true));  //Проверяем, что успешный запрос возвращает ok: true;


    }


}
