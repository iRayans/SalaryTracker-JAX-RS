package com.rayan.salarytracker.tests.api;

import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
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
    private Jsonb jsonb;
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
        jsonb = JsonbBuilder.create();
    }

    public AuthServiceTest() {
        init();
    }


    public void register() {
        LOGGER.info("Registering user...");
        String registerUrl = baseUrl + "register";
        Map<String, String> payload = Map.of(
                "email", email,
                "password", password,
                "confirmPassword", "test123&",
                "role", "ADMIN"
        );
        String registerJson = jsonb.toJson(payload);
        Response response = client.target(registerUrl)
                .request()
                .post(Entity.entity(registerJson, MediaType.APPLICATION_JSON));


    }

    public void login() {
        LOGGER.info("Login user...");
        String loginPath = baseUrl + "login";

        Jsonb jsonb = JsonbBuilder.create();

        // Create JSON payload as a Map
        Map<String, String> payload = Map.of(
                "email", email,
                "password", password
        );

        // Convert to JSON string using
        String loginJson = jsonb.toJson(payload);

        // Send POST request to login endpoint
        Response response = client.target(loginPath)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(loginJson, MediaType.APPLICATION_JSON));

        // Read JSON response as String
        String jsonResponse = response.readEntity(String.class);

        // Parse JSON response
        JsonObject jsonNode = jsonb.fromJson(jsonResponse, JsonObject.class);

        // Extract values
        this.jwtToken = jsonNode.getString("token");
        this.userId = jsonNode.getJsonObject("user").getJsonNumber("id").toString();
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
