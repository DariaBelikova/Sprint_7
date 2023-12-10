package edu.praktikum.sprint7.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String CREATE_ORDER_URL = "/api/v1/orders";
    private static final String ORDER_LIST_URL = "/api/v1/orders?courierId=%s";

    private static int id;

    @Step("Создание заказа {order}")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(CREATE_ORDER_URL);
    }

    @Step("Получение списка заказов")
        public static Response getOrderList (int id) {
            return given()
                    .header("Content-type", "application/json")
                    .when()
                    .get(String.format(ORDER_LIST_URL, id));
        }

}
