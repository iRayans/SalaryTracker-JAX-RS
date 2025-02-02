package com.rayan.salarytracker.tests.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SalaryTrackerIT {
    private static String baseUrl;
    private static String jwtToken;

    @BeforeAll
    public static void init() {
        String port = "9080";
        baseUrl = "http://localhost:" + port + "/" + "api";
    }

    @Test
    public void testLoginAndGetJWT() throws JsonProcessingException {
        Client client = ClientBuilder.newClient();
        String loginPath = baseUrl + "/auth/login";
        String loginPayload = "{\"email\":\"tester@ibm.com\", " +
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
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        String jwtToken = jsonNode.get("token").asText();

        // Ensure token is not null or empty
        assertNotNull(jwtToken, "JWT token should not be null");
        assertFalse(jwtToken.isEmpty(), "JWT token should not be empty");

        client.close();
    }
}
