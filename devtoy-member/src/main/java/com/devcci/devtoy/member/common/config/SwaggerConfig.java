package com.devcci.devtoy.member.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@OpenAPIDefinition(info = @Info(title = "[DEV TOY - MEMBER] 혼자 놀아보기", description = "서비스의 API 명세입니다.",
    version = "v1.0.0"))
@Configuration
public class SwaggerConfig {
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
            .components(
                new Components().addSecuritySchemes("bearerAuth", securityScheme))
            .security(Collections.singletonList(securityRequirement))
            .addServersItem(new Server().url("/"))
            ;
    }
}
