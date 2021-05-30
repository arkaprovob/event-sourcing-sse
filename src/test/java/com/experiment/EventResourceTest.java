package com.experiment;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class EventResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/events/message/hello")
                .then()
                .statusCode(200)
                .body(is("message published successfully"));
    }

}