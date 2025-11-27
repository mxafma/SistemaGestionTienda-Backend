package com.tienda.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Value("${openapi.server.url:}")
    private String openapiServerUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info().title("Tienda Backend API").version("v1"));

        if (openapiServerUrl != null && !openapiServerUrl.isBlank()) {
            Server server = new Server();
            server.setUrl(openapiServerUrl);
            server.setDescription("Configured server");
            openAPI.addServersItem(server);
        } else {
            Server local = new Server();
            local.setUrl("http://localhost:8080");
            local.setDescription("Local Development");
            openAPI.addServersItem(local);
        }

        return openAPI;
    }
}
