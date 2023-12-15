package edu.praktikum.sprint7;

import edu.praktikum.sprint7.order.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateOrderTests {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int dateOrder;
    private final String deliveryDate;
    private final String comment;
    private final List<String> color;


    public CreateOrderTests(String firstName, String lastName, String address, int metroStation, String phone, int dateOrder, String deliveryDate, String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.dateOrder = dateOrder;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters()
    public static Object[][] params() {
        return new Object[][]{
                {"Severus", "Snape", "Spiders End", 4, "+71234567890", 5, "2023-12-10", "Always.", List.of("BLACK")},
                {"Tom", "Riddle", "Nowhere", 4, "+70987654321", 5, "2020-12-10", "Out Of Fear, Not Loyalty.", List.of("GREY")},
                {"Tom", "Riddle", "Nowhere", 4, "+70987654321", 5, "2020-12-10", "Out Of Fear, Not Loyalty.", List.of("BLACK", "GREY")},
                {"Severus", "Snape", "Spiders End", 4, "+71234567890", 5, "2023-12-10", "Always.", List.of()},
        };
    }

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    private int track;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Проверка выбора цветов самоката")
    @Description("Можно указть оба цвета, один из уветов или не указывать увет. Метод вернет track.")
    public void checkOrdersTreckTest() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, dateOrder, deliveryDate, comment);
        OrderClient courierClient = new OrderClient();
        Response createOrder = courierClient.createOrder(order);

        assertEquals("Неверный статус код", SC_CREATED, createOrder.statusCode());

        track = createOrder.path("track");


    }

}
