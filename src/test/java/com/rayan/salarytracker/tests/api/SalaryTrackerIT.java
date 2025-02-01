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
    private static String siteURL;
    private static String jwtToken;

    @BeforeAll
    public static void init() {
        String port = "9080";  // Make sure Open Liberty is running on this port
        String war = "api";    // Adjust based on your application
        siteURL = "http://localhost:" + port + "/" + war + "/auth/login";
    }

    @Test
    public void testLoginAndGetJWT() throws JsonProcessingException {
        // Create a JAX-RS client
        Client client = ClientBuilder.newClient();

        // Define login credentials as JSON
        String loginPayload = "{\"email\":\"tester@ibm.com\", " +
                "\"password\":\"test123&\"}";

        // Send POST request to login endpoint
        Response response = client.target(siteURL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(loginPayload));

        // Check HTTP status
        assertEquals(200, response.getStatus(), "Login request failed!");

        // Read JSON response as String
        String jsonResponse = response.readEntity(String.class);
        System.out.println("Raw JSON Response: " + jsonResponse);

        // Parse JSON using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        String jwtToken = jsonNode.get("token").asText();
        System.out.println("JWT Token: " + jwtToken);

        // Ensure token is not null or empty
        assertNotNull(jwtToken, "JWT token should not be null");
        assertFalse(jwtToken.isEmpty(), "JWT token should not be empty");

        // Close client
        client.close();
    }
}
