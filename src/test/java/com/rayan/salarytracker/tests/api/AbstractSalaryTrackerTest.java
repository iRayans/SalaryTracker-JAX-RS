package com.rayan.salarytracker.tests.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractSalaryTrackerTest {
    private Client client;
    private String port;
    private String baseUrl;
    private String email;
    private String password;

    @Getter
    private String jwtToken;
    @Getter
    private String userId;


    public void init() {
        String port = "9080";
        baseUrl = "http://localhost:" + port + "/" + "api/auth/";
        client = ClientBuilder.newClient();
        email = "test@gmail.com";
        password = "test123&";

    }


    public void register() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String registerUrl = baseUrl + "register";
        Map<String, String> payload = Map.of(
                "email", email,
                "password", password,
                "confirmPassword", "test123&"
        );
        String salaryJson = objectMapper.writeValueAsString(payload);
        Response response = client.target(registerUrl)
                .request()
                .post(Entity.entity(salaryJson, MediaType.APPLICATION_JSON));

        assertEquals(201, response.getStatus(), "Register request failed!");

    }

    public void login() {
        String loginPath = baseUrl + "login";
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
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(jsonResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.jwtToken = jsonNode.get("token").asText();
        this.userId = jsonNode.get("user").get("id").asText();

        // Ensure token is not null or empty
        assertNotNull(jwtToken, "JWT token should not be null");
        assertFalse(jwtToken.isEmpty(), "JWT token should not be empty");

        deleteUser();
    }

    public void deleteUser() {
        client.target(baseUrl + "users/" + userId);
        Response response = client.target(baseUrl + "users/" + userId).request().delete();

    }

}
