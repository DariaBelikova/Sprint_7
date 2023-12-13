package edu.praktikum.sprint7;

import edu.praktikum.sprint7.courier.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Courier;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateWithoutPasswordTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
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


    }
}
