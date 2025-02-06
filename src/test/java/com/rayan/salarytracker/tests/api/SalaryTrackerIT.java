package com.rayan.salarytracker.tests.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rayan.salarytracker.model.Salary;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SalaryTrackerIT {
    private static String baseUrl;

    private static Client client;
    private static AuthServiceTest auth = new AuthServiceTest();
    private String authHeader;
    private static Long salaryId;

    @BeforeAll
    public static void init() throws JsonProcessingException {
        String port = "9080";
        baseUrl = "http://localhost:" + port + "/" + "api";
        client = ClientBuilder.newClient();

        auth.register();
        auth.login();
    }

    @AfterAll
    public static void tearDown() {
        client.close();
    }


    private Salary createSalary() {
        Salary salary = new Salary();
        salary.setDescription("test description");
        salary.setAmount(5000);
        salary.setMonth("April");
        return salary;
    }

    @Test
    @Order(1)
    public void insertSalary() throws JsonProcessingException {
        String insertSalaryPath = baseUrl + "/salaries";
        Salary salary = createSalary();

        ObjectMapper objectMapper = new ObjectMapper();
        // serialize Salary object to JSON format
        String salaryJson = objectMapper.writeValueAsString(salary);

        Response response = client.target(insertSalaryPath)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + auth.getJwtToken())
                .post(Entity.entity(salaryJson, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus(), "Insert salary failed!");

    }

    @Test
    @Order(2)
    public void getAllSalaries() throws JsonProcessingException {
        String salariesPath = baseUrl + "/salaries";

        Response response = client.target(salariesPath)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + auth.getJwtToken())
                .get();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = response.readEntity(String.class);

        List<Salary> salaries = objectMapper.readValue(jsonResponse, new TypeReference<List<Salary>>() {
        });

        Salary salary = salaries.get(0);
        salaryId = salaries.get(0).getId();

        assertEquals(200, response.getStatus(), "Salaries request failed!");

    }

    @Test
    @Order(3)
    public void updateSalary() {
        String updateSalaryPath = baseUrl + "/salaries/" + salaryId;
        String updatePayload = "{\"month\":\"November\"}";

        Response response = client.target(updateSalaryPath)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + auth.getJwtToken())
                .put(Entity.json(updatePayload));

        assertEquals(200, response.getStatus(), "Update request failed!");
    }

    @Test
    @Order(4)
    public void getExpenses() {
        String expensesPath = baseUrl + "/expenses/" + auth.getUserId();
        Response response = client.target(expensesPath)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + auth.getJwtToken())
                .get();

        assertEquals(200, response.getStatus(), "Expenses request failed!");
    }

    @Test
    @Order(5)
    public void deleteSalary() {
        String salariesPath = baseUrl + "/salaries/" + salaryId;
        Response response = client.target(salariesPath)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + auth.getJwtToken())
                .delete();
        assertEquals(204, response.getStatus(), "Delete salary failed!");

        auth.deleteUser();
    }


}
