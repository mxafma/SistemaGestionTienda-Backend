package com.tienda.backend.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
    servers = {
        @Server(
            url = "https://scintillating-renewal-production.up.railway.app",
            description = "Railway Production"
        )
    }
)
public class OpenApiConfig {
}
