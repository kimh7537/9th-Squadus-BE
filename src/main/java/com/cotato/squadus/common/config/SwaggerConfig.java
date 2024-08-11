package com.cotato.squadus.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@OpenAPIDefinition(
        info = @Info(title = "Squadus 프로젝트 API 명세서",
                description = "Squadus API 명세서",
                version = "v1")
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        String headerName = "access";

        // Define the SecurityRequirement to be included in the request
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(headerName);

        Components components = new Components()
                .addSecuritySchemes(headerName, new SecurityScheme()
                        .name(headerName)
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name(headerName));

        return new OpenAPI()
                .addServersItem(new Server().url("http://localhost:8080").description("Local Server"))
                .addServersItem(new Server().url("http://15.165.165.240:8080").description("AWS Server"))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
