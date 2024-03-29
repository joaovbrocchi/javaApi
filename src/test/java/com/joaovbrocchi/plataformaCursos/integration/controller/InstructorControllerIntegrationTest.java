package com.joaovbrocchi.plataformaCursos.integration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaovbrocchi.plataformaCursos.config.TestConfig;
import com.joaovbrocchi.plataformaCursos.entity.Instructor;
import com.joaovbrocchi.plataformaCursos.integration.container.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class InstructorControllerIntegrationTest extends AbstractIntegrationTest {
        private static RequestSpecification requestSpecification;
        private static ObjectMapper objectMapper;
        private static Instructor instructor;

        @BeforeAll
        public static void setup(){
            objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            requestSpecification = new RequestSpecBuilder()
                    .setBasePath("/instructor")
                    .setPort(TestConfig.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                    .build();
            instructor = new Instructor(
                    "joao",
                    "brocchi",
                    "email123@email.com"
            );
        }
    private void assertObjectFieldsNotNull(Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(object);
            assertNotNull(value, "Field '" + field.getName() + "' is null");
        }
    }

        @Test
        @Order(1)
        @DisplayName("teste de integração do create do do controller de Instructor")
        void integrationTestGivenInstructor_when_InstructorCreate_ShouldReturnTheCreatedObject() throws JsonProcessingException, IllegalAccessException {
            var content =
                    given()
                        .spec(requestSpecification)
                        .contentType(TestConfig.CONTENT_TYPE_JSON)
                        .body(instructor)
                    .when()
                        .post()
                    .then()
                        .statusCode(200)
                            .extract()
                                .body()
                                    .asString();
            Instructor createdInstructor = objectMapper.readValue(content, Instructor.class);

            instructor = createdInstructor;

            assertObjectFieldsNotNull(createdInstructor);
            assertTrue(createdInstructor.getId() > 0);




        }
    @Test
    @Order(2)
    @DisplayName("teste de integração do  update do controller de Instructor")
    void integrationTestGivenInstructor_when_InstructorUpdate_ShouldReturnTheUpdatedObject(){
            instructor.setFirstName("lucas");
            instructor.setEmail("novo123@email.com");

        var content =
                given()
                    .spec(requestSpecification)
                    .contentType(TestConfig.CONTENT_TYPE_JSON)
                    .body(instructor)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .asString();
    }

    @Test
    @Order(3)
    @DisplayName("teste de integração do  update do controller de Instructor")
    void integrationTestGivenInstructor_when_InstructorFindById_ShouldReturnTheObject(){
        instructor.setFirstName("lucas");
        instructor.setEmail("novo123@email.com");

        var content =
                given()
                    .spec(requestSpecification)
                    .pathParam("id", instructor.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .asString();
        }

    @Test
    @Order(3)
    @DisplayName("teste de integração do  update do controller de Instructor")
    void integrationTestGivenInstructor_when_InstructorFindAll_ShouldReturnAllTheObject() throws JsonProcessingException {

        var content =
                given()
                    .spec(requestSpecification)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .asString();
        Instructor[] myArray =
                objectMapper
                        .readValue(content, Instructor[].class);
        List<Instructor> instructors =  Arrays.asList(myArray);



        }
    @Test
    @Order(3)
    @DisplayName("teste de integração do  update do controller de Instructor")
    void integrationTestGivenInstructor_when_InstructorDelte_ShouldReturnNoContent(){
        var content =
            given()
                .spec(requestSpecification)
                .pathParam("id", instructor.getId())
            .when()
                .delete("{id}")
            .then()
                .statusCode(204);
}

    }

