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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class AuthServiceTest {
    private Client client;
    private String port;
    private String baseUrl;
    private String email;
    private String password;
    private ObjectMapper objectMapper;
    @Getter
    private String jwtToken;
    @Getter
    private String userId;

    private static final Logger LOGGER = LogManager.getLogger(AuthServiceTest.class.getName());

    public void init() {
        String port = "9080";
        baseUrl = "http://localhost:" + port + "/" + "api/auth/";
        client = ClientBuilder.newClient();
        email = "testerw@gmail.com";
        password = "test123&";
        objectMapper = new ObjectMapper();
    }

    public AuthServiceTest() {
        init();
    }


    public void register() throws JsonProcessingException {
        LOGGER.info("Registering user...");
        String registerUrl = baseUrl + "register";
        Map<String, String> payload = Map.of(
                "email", email,
                "password", password,
                "confirmPassword", "test123&",
                "role", "ADMIN"
        );
        String registerJson = objectMapper.writeValueAsString(payload);
        Response response = client.target(registerUrl)
                .request()
                .post(Entity.entity(registerJson, MediaType.APPLICATION_JSON));


    }

    public void login() throws JsonProcessingException {
        LOGGER.info("Login user...");
        String loginPath = baseUrl + "login";
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> payload = Map.of(
                "email", email,
                "password", password
        );

        String loginJson = objectMapper.writeValueAsString(payload);
        // Send POST request to login endpoint
        Response response = client.target(loginPath)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(loginJson, MediaType.APPLICATION_JSON));

        // Read JSON response as String
        String jsonResponse = response.readEntity(String.class);

        // Parse JSON using Jackson

        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(jsonResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.jwtToken = jsonNode.get("token").asText();
        this.userId = jsonNode.get("user").get("id").asText();


    }

    public void deleteUser() {
        LOGGER.info("Deleting user ...");
        String deleteUserPath = "http://localhost:9080/api/users/" + userId;
        client.target(deleteUserPath);
        Response response = client.target(deleteUserPath)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + getJwtToken())
                .delete();
    }

}
