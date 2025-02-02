package com.rayan.salarytracker.tests.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SalaryTrackerIT {
    private static String baseUrl;
    private static String jwtToken;
    private static Client client;
    private static int userId;
    private String authHeader;

    @BeforeAll
    public static void init() {
        String port = "9080";
        baseUrl = "http://localhost:" + port + "/" + "api";
        client = ClientBuilder.newClient();

        loginAndExtractToken();
    }

    @AfterAll
    public static void tearDown() {
        client.close(); // ✅ Close client after all tests
    }


    private static void loginAndExtractToken() {
        client = ClientBuilder.newClient();
        String loginPath = baseUrl + "/auth/login";
        String loginPayload = "{\"email\":\"rayan@gmail.com\", " +
                "\"password\":\"test123&\"}";

        // Send POST request to login endpoint
        Response response = client.target(loginPath)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(loginPayload));

        // Check HTTP status
        assertEquals(200, response.getStatus(), "Login request failed!");

        // Read JSON response as String
        String jsonResponse = response.readEntity(String.class);

        // Parse JSON using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(jsonResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        jwtToken = jsonNode.get("token").asText();
        String userId = jsonNode.get("user").get("id").asText();

        // Ensure token is not null or empty
        assertNotNull(jwtToken, "JWT token should not be null");
        assertFalse(jwtToken.isEmpty(), "JWT token should not be empty");

    }

    @Test
    public void getAllSalaries() {
        client = ClientBuilder.newClient();
        String salariesPath = baseUrl + "/salaries";

        Response response = client.target(salariesPath)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .get();

        assertEquals(200, response.getStatus(), "Salaries request failed!");

    }

    @Test
    public void getExpenses() {
        client = ClientBuilder.newClient();
        String expensesPath = baseUrl + "/expenses/" + userId;
        System.out.println("user id " + userId);
        Response response = client.target(expensesPath)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .get();

        assertEquals(200, response.getStatus(), "Expenses request failed!");

        String jsonResponse = response.readEntity(String.class);
        System.out.println("Expenses Response: " + jsonResponse);
    }
}
