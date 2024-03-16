package com.users.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme securitySchemeBasic = getBasicSecurityScheme();
        SecurityScheme securitySchemeBearer = getBearerSecurityScheme();

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(securitySchemeBasic.getName(), securitySchemeBasic)
                        .addSecuritySchemes(securitySchemeBearer.getName(), securitySchemeBearer))
                .info(new Info()
                        .title("Users API")
                        .description("CRUD API for users")
                        .summary("summary").version("v1"));
    }

    private static SecurityScheme getBasicSecurityScheme() {
        SecurityScheme securitySchemeBasic = new SecurityScheme();
        securitySchemeBasic.setName("Basic Authentication");
        securitySchemeBasic.scheme("basic");
        securitySchemeBasic.type(SecurityScheme.Type.HTTP);
        securitySchemeBasic.in(SecurityScheme.In.HEADER);
        return securitySchemeBasic;
    }

    private static SecurityScheme getBearerSecurityScheme() {
        SecurityScheme securitySchemeBearer = new SecurityScheme();
        securitySchemeBearer.setName("Bearer Authentication");
        securitySchemeBearer.scheme("bearer");
        securitySchemeBearer.type(SecurityScheme.Type.HTTP);
        securitySchemeBearer.in(SecurityScheme.In.HEADER);
        return securitySchemeBearer;
    }
}
